package de.barpos.kassensystem.domain.usecase.storno

import de.barpos.kassensystem.domain.model.StornoGrund
import de.barpos.kassensystem.domain.model.Transaktion
import de.barpos.kassensystem.domain.model.TransaktionsPosition
import de.barpos.kassensystem.domain.repository.IArtikelRepository
import de.barpos.kassensystem.domain.repository.ITransaktionRepository
import de.barpos.kassensystem.domain.tse.TseService
import javax.inject.Inject

/**
 * T058 - Use-case to cancel an entire open transaction.
 */
class StorniereTransaktionUseCase @Inject constructor(
    private val transaktionRepository: ITransaktionRepository,
    private val artikelRepository: IArtikelRepository,
    private val tseService: TseService
) {
    suspend operator fun invoke(transaktionId: Long, grund: StornoGrund): Transaktion? {
        val transaktion = transaktionRepository.findById(transaktionId) ?: return null
        val positionen = transaktionRepository.positionenFuerTransaktion(transaktionId)

        // 1. Cancel transaction with TSE
        val stornierteTransaktion = tseService.cancelTransaction(transaktion)

        // 2. Update transaction in DB (or move to a separate "Storno" table)
        // For simplicity, we just update the status. A real system might do more.
        transaktionRepository.aktualisieren(stornierteTransaktion)

        // 3. Restore inventory for all positions
        positionen.forEach { position ->
            artikelRepository.bestandErhoehen(position.skuId, position.menge)
        }

        // 4. Create a cancellation receipt (Stornobeleg)
        // This would be similar to creating a normal receipt but for a cancellation.
        // tseService.createCancellationReceipt(...)

        return stornierteTransaktion
    }
}
