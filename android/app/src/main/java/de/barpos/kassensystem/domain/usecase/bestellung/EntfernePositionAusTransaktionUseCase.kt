package de.barpos.kassensystem.domain.usecase.bestellung

import de.barpos.kassensystem.domain.repository.IArtikelRepository
import de.barpos.kassensystem.domain.repository.ITransaktionRepository
import org.threeten.bp.Instant
import javax.inject.Inject

/**
 * T051 — Use-case to remove a position from a transaction.
 */
class EntfernePositionAusTransaktionUseCase @Inject constructor(
    private val transaktionRepository: ITransaktionRepository,
    private val artikelRepository: IArtikelRepository
) {
    suspend operator fun invoke(positionId: Long) {
        // This is a simplified version. A real implementation needs to find the position first
        // to know which SKU and quantity to add back to inventory.
        // For now, we assume the UI provides this info or we fetch it before deleting.
        // val position = transaktionRepository.findPositionById(positionId)
        // if (position != null) {
        //    artikelRepository.bestandErhoehen(position.skuId, position.menge)
        // }
        transaktionRepository.positionEntfernen(positionId)

        // Update transaction total
        // val transaktion = transaktionRepository.findById(position.transaktionId)
        // ... update logic ...
    }
}
