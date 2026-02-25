package de.barpos.kassensystem.domain.usecase

import de.barpos.kassensystem.domain.model.Transaktion
import de.barpos.kassensystem.domain.repository.ITransaktionRepository
import javax.inject.Inject

class SaveTransaktionUseCase @Inject constructor(
    private val transaktionRepository: ITransaktionRepository,
    private val tseSignUseCase: TseSignUseCase
) {
    suspend fun execute(transaktion: Transaktion) {
        transaktionRepository.save(transaktion)
        tseSignUseCase.execute(transaktion)
    }
}
