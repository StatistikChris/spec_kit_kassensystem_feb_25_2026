package de.barpos.kassensystem.data.repository

import de.barpos.kassensystem.data.db.dao.PreisregelDao
import de.barpos.kassensystem.data.mapper.toDomain
import de.barpos.kassensystem.data.mapper.toEntity
import de.barpos.kassensystem.domain.model.Preisregel
import de.barpos.kassensystem.domain.repository.IPreisregelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreisregelRepositoryImpl @Inject constructor(
    private val dao: PreisregelDao
) : IPreisregelRepository {

    override suspend fun erstellen(preisregel: Preisregel): Preisregel {
        val id = dao.insert(preisregel.toEntity())
        return preisregel.copy(id = id)
    }

    override suspend fun aktualisieren(preisregel: Preisregel) {
        dao.update(preisregel.toEntity())
    }

    override suspend fun deaktivieren(id: Long) {
        dao.deaktivieren(id)
    }

    override suspend fun findById(id: Long): Preisregel? =
        dao.findById(id)?.toDomain()

    override fun beobachteAktive(): Flow<List<Preisregel>> =
        dao.beobachteAktive().map { it.map { e -> e.toDomain() } }

    override fun beobachteAlle(): Flow<List<Preisregel>> =
        dao.beobachteAlle().map { it.map { e -> e.toDomain() } }
}
