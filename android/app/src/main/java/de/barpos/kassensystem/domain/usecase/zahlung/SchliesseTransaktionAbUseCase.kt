package de.barpos.kassensystem.domain.usecase.zahlung

import de.barpos.kassensystem.domain.model.Beleg
import de.barpos.kassensystem.domain.model.TischStatus
import de.barpos.kassensystem.domain.model.Transaktion
import de.barpos.kassensystem.domain.model.TransaktionStatus
import de.barpos.kassensystem.domain.model.Zahlungsart
import de.barpos.kassensystem.domain.repository.IBelegRepository
import de.barpos.kassensystem.domain.repository.ITransaktionRepository
import de.barpos.kassensystem.domain.tse.TseService
import de.barpos.kassensystem.domain.usecase.tischplan.UpdateTischStatusUseCase
import org.threeten.bp.Instant
import java.util.UUID
import javax.inject.Inject

/**
 * T055 - Use-case to finalize a transaction after successful payment.
 */
class SchliesseTransaktionAbUseCase @Inject constructor(
    private val transaktionRepository: ITransaktionRepository,
    private val belegRepository: IBelegRepository,
    private val tseService: TseService,
    private val updateTischStatusUseCase: UpdateTischStatusUseCase
) {
    suspend operator fun invoke(transaktionId: Long, zahlungsart: Zahlungsart): Beleg? {
        val transaktion = transaktionRepository.findById(transaktionId) ?: return null
        if (transaktion.status != TransaktionStatus.IN_BEZAHLUNG) return null

        // 1. Finalize transaction with TSE
        val finishedTransaction = tseService.finishTransaction(transaktion)

        // 2. Update transaction in DB
        val abgeschlosseneTransaktion = finishedTransaction.copy(
            status = TransaktionStatus.ABGESCHLOSSEN,
            zahlungsart = zahlungsart,
            updatedAt = Instant.now()
        )
        transaktionRepository.aktualisieren(abgeschlosseneTransaktion)

        // 3. Create and save the receipt (Beleg)
        val beleg = createBelegFromTransaktion(abgeschlosseneTransaktion)
        val finalBeleg = belegRepository.erstellen(beleg)

        // 4. Update table status
        updateTischStatusUseCase(transaktion.tischId, TischStatus.FREI)

        return finalBeleg
    }

    private fun createBelegFromTransaktion(transaktion: Transaktion): Beleg {
        return Beleg(
            uuid = UUID.randomUUID().toString(),
            transaktionId = transaktion.id,
            tseClientId = transaktion.tseClientId,
            tseTransaktionId = transaktion.tseTransaktionId,
            tseSignatureCounter = transaktion.tseSignatureCounter,
            tseSignature = transaktion.tseSignature,
            tseTimestamp = transaktion.tseTimestamp,
            qrCodeData = "TODO: Generate QR Code Data", // TODO
            createdAt = Instant.now()
        )
    }
}
