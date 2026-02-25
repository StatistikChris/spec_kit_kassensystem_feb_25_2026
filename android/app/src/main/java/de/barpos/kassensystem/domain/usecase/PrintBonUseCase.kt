package de.barpos.kassensystem.domain.usecase

import de.barpos.kassensystem.domain.repository.IBonDrucker
import de.barpos.kassensystem.domain.repository.IBonRepository
import javax.inject.Inject

class PrintBonUseCase @Inject constructor(
    private val bonRepository: IBonRepository,
    private val bonDrucker: IBonDrucker
) {
    suspend fun execute(bonId: Long) {
        // TODO: Implement use case
    }
}
