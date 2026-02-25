package de.barpos.kassensystem.data.repository

import de.barpos.kassensystem.data.db.dao.PreisregelDao
import de.barpos.kassensystem.domain.model.Preisregel
import de.barpos.kassensystem.domain.repository.IPreisregelRepository
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import javax.inject.Inject

class PreisregelRepository @Inject constructor(
    private val preisregelDao: PreisregelDao
) : IPreisregelRepository {
    override fun getAll(): Flow<List<Preisregel>> {
        TODO("Not yet implemented")
    }

    override fun getActiveRulesAt(time: Instant): Flow<List<Preisregel>> {
        TODO("Not yet implemented")
    }

    override suspend fun insert(preisregel: Preisregel) {
        TODO("Not yet implemented")
    }

    override suspend fun update(preisregel: Preisregel) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(preisregel: Preisregel) {
        TODO("Not yet implemented")
    }
}
