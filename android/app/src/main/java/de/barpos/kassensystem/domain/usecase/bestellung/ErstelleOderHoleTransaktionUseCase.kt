package de.barpos.kassensystem.domain.usecase.bestellung

import de.barpos.kassensystem.domain.model.Transaktion
import de.barpos.kassensystem.domain.model.TransaktionStatus
import de.barpos.kassensystem.domain.model.Zahlungsart
import de.barpos.kassensystem.domain.repository.ITransaktionRepository
import org.threeten.bp.Instant
import java.util.UUID
import javax.inject.Inject

/**
 * T049 — Use-case to get an existing open transaction for a table or create a new one.
 */
class ErstelleOderHoleTransaktionUseCase @Inject constructor(
    private val transaktionRepository: ITransaktionRepository
) {
    suspend operator fun invoke(tischId: Long, bedienerId: Long, schichtId: Long): Transaktion {
        val existing = transaktionRepository.findOffeneTransaktionByTisch(tischId)
        if (existing != null) {
            return existing
        }

        val now = Instant.now()
        val neueTransaktion = Transaktion(
            uuid = UUID.randomUUID().toString(),
            tischId = tischId,
            bedienerId = bedienerId,
            schichtId = schichtId,
            status = TransaktionStatus.OFFEN,
            zahlungsart = Zahlungsart.UNBEKANNT,
            gesamtInCent = 0L,
            createdAt = now,
            updatedAt = now
        )
        return transaktionRepository.erstellen(neueTransaktion)
    }
}
