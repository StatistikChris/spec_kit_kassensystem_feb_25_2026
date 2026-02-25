package de.barpos.kassensystem.domain.usecase.tischplan

import de.barpos.kassensystem.domain.model.Tisch
import de.barpos.kassensystem.domain.repository.ITischRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * T039 — Use-case to observe the list of all tables.
 * Exposes the Flow directly from the repository.
 */
class BeobachteTischeUseCase @Inject constructor(
    private val tischRepository: ITischRepository
) {
    operator fun invoke(): Flow<List<Tisch>> = tischRepository.beobachteAlle()
}
