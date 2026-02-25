package de.barpos.kassensystem.domain.usecase

import de.barpos.kassensystem.domain.repository.IBonDrucker
import javax.inject.Inject

class PrintZBonUseCase @Inject constructor(
    private val bonDrucker: IBonDrucker
) {
    suspend fun execute(zBonId: Long) {
        // TODO: Implement use case
    }
}
