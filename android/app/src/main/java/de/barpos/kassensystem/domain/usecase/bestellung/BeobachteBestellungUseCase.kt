package de.barpos.kassensystem.domain.usecase.bestellung

import de.barpos.kassensystem.domain.model.Transaktion
import de.barpos.kassensystem.domain.model.TransaktionsPosition
import de.barpos.kassensystem.domain.repository.ITransaktionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class Bestellung(
    val transaktion: Transaktion,
    val positionen: List<TransaktionsPosition>
)

/**
 * T042 - Use-case to observe a transaction and its positions.
 */
class BeobachteBestellungUseCase @Inject constructor(
    private val transaktionRepository: ITransaktionRepository
) {
    operator fun invoke(transaktionId: Long): Flow<Bestellung?> {
        return combine(
            transaktionRepository.beobachteTransaktion(transaktionId),
            transaktionRepository.beobachtePositionenFuerTransaktion(transaktionId)
        ) { transaktion, positionen ->
            transaktion?.let {
                Bestellung(it, positionen)
            }
        }
    }
}
