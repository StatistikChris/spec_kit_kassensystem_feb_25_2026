package de.barpos.kassensystem.domain.usecase

import de.barpos.kassensystem.data.db.dao.BonDao
import de.barpos.kassensystem.domain.repository.TseClient
import javax.inject.Inject

class StornoUseCase @Inject constructor(
    private val tseClient: TseClient,
    private val bonDao: BonDao
) {
    suspend fun execute(transaktionId: Long) {
        if (bonDao.findByTransaktionId(transaktionId) != null) {
            throw StornoNachBonException("Storno nicht möglich, da bereits ein Bon gedruckt wurde.")
        }
        // TODO: Implement storno logic
    }
}

class StornoNachBonException(message: String) : Exception(message)
