package de.barpos.kassensystem.domain.usecase.zahlung

import de.barpos.kassensystem.domain.model.TischStatus
import de.barpos.kassensystem.domain.model.TransaktionStatus
import de.barpos.kassensystem.domain.repository.ITransaktionRepository
import de.barpos.kassensystem.domain.usecase.tischplan.UpdateTischStatusUseCase
import org.threeten.bp.Instant
import javax.inject.Inject

/**
 * T054 - Use-case to initiate the payment process for a transaction.
 */
class StarteZahlungUseCase @Inject constructor(
    private val transaktionRepository: ITransaktionRepository,
    private val updateTischStatusUseCase: UpdateTischStatusUseCase
) {
    suspend operator fun invoke(transaktionId: Long) {
        val transaktion = transaktionRepository.findById(transaktionId) ?: return
        if (transaktion.status == TransaktionStatus.OFFEN) {
            transaktionRepository.aktualisieren(
                transaktion.copy(
                    status = TransaktionStatus.IN_BEZAHLUNG,
                    updatedAt = Instant.now()
                )
            )
            updateTischStatusUseCase(transaktion.tischId, TischStatus.ZAHLEND)
        }
    }
}
