package de.barpos.kassensystem.domain.usecase

import de.barpos.kassensystem.domain.repository.IBonDrucker
import javax.inject.Inject

class PrintSchichtberichtUseCase @Inject constructor(
    private val bonDrucker: IBonDrucker
) {
    suspend fun execute(schichtId: Long) {
        // TODO: Implement use case
    }
}
