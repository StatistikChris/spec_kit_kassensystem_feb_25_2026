package de.barpos.kassensystem.domain.usecase.tischplan

import de.barpos.kassensystem.domain.repository.ITischRepository
import javax.inject.Inject

/**
 * T040 — Use-case to update the status of a table.
 * Encapsulates the optimistic locking logic.
 */
class UpdateTischStatusUseCase @Inject constructor(
    private val tischRepository: ITischRepository
) {
    /**
     * @return true on success, false if the version has changed (optimistic lock failed).
     */
    suspend operator fun invoke(tischId: Long, neuerStatus: String, erwartetVersion: Long): Boolean {
        return tischRepository.statusAktualisieren(tischId, neuerStatus, erwartetVersion)
    }
}
