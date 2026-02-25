package de.barpos.kassensystem.domain.usecase.storno

import de.barpos.kassensystem.domain.model.StornoGrund
import de.barpos.kassensystem.domain.repository.IArtikelRepository
import de.barpos.kassensystem.domain.repository.ITransaktionRepository
import de.barpos.kassensystem.domain.tse.TseService
import org.threeten.bp.Instant
import javax.inject.Inject

/**
 * T059 - Use-case to cancel a single position from an open transaction.
 */
class StornierePositionUseCase @Inject constructor(
    private val transaktionRepository: ITransaktionRepository,
    private val artikelRepository: IArtikelRepository,
    private val tseService: TseService
) {
    suspend operator fun invoke(positionId: Long, grund: StornoGrund) {
        val position = transaktionRepository.findPositionById(positionId) ?: return
        val transaktion = transaktionRepository.findById(position.transaktionId) ?: return

        // 1. Update TSE with a negative item (Storno-Position)
        // This is a simplification. The TSE might require a specific "update" call.
        tseService.addOrUpdatePosition(
            transaktion,
            position.copy(
                menge = -position.menge,
                gesamtpreisInCent = -position.gesamtpreisInCent
            )
        )

        // 2. Remove the position from the database
        transaktionRepository.positionEntfernen(positionId)

        // 3. Restore inventory
        artikelRepository.bestandErhoehen(position.skuId, position.menge)

        // 4. Update transaction total
        val allePositionen = transaktionRepository.positionenFuerTransaktion(transaktion.id)
        val neuerGesamt = allePositionen.sumOf { it.gesamtpreisInCent }
        transaktionRepository.aktualisieren(transaktion.copy(gesamtInCent = neuerGesamt, updatedAt = Instant.now()))
    }
}
