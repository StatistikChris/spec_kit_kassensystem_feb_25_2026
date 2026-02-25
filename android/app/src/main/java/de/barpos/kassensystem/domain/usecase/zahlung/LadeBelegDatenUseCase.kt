package de.barpos.kassensystem.domain.usecase.zahlung

import de.barpos.kassensystem.domain.model.Beleg
import de.barpos.kassensystem.domain.model.TransaktionsPosition
import de.barpos.kassensystem.domain.repository.IBelegRepository
import de.barpos.kassensystem.domain.repository.IBedienerRepository
import de.barpos.kassensystem.domain.repository.ITransaktionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class BelegDaten(
    val beleg: Beleg,
    val positionen: List<TransaktionsPosition>,
    val bedienerName: String
)

/**
 * T056 - Use-case to get all data required for printing a receipt.
 */
class LadeBelegDatenUseCase @Inject constructor(
    private val belegRepository: IBelegRepository,
    private val transaktionRepository: ITransaktionRepository,
    private val bedienerRepository: IBedienerRepository
) {
    suspend operator fun invoke(belegId: Long): BelegDaten? {
        val beleg = belegRepository.findById(belegId) ?: return null
        val transaktion = transaktionRepository.findById(beleg.transaktionId) ?: return null
        val positionen = transaktionRepository.positionenFuerTransaktion(beleg.transaktionId)
        val bediener = bedienerRepository.findById(transaktion.bedienerId) ?: return null

        return BelegDaten(
            beleg = beleg,
            positionen = positionen,
            bedienerName = bediener.name
        )
    }
}
