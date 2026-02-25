package de.barpos.kassensystem.domain.usecase.bestellung

import de.barpos.kassensystem.domain.model.TransaktionsPosition
import javax.inject.Inject

/**
 * T052 — Pure function use-case to calculate the total of a list of positions.
 */
class BerechneWarenkorbUseCase @Inject constructor() {
    operator fun invoke(positionen: List<TransaktionsPosition>): Long {
        return positionen.sumOf { it.gesamtpreisInCent }
    }
}
