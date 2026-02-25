package de.barpos.kassensystem.data.repository

import de.barpos.kassensystem.data.db.dao.BedienerDao
import de.barpos.kassensystem.data.mapper.toDomain
import de.barpos.kassensystem.data.mapper.toEntity
import de.barpos.kassensystem.domain.model.Bediener
import de.barpos.kassensystem.domain.repository.IBedienerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BedienerRepositoryImpl @Inject constructor(
    private val dao: BedienerDao
) : IBedienerRepository {

    override suspend fun erstellen(bediener: Bediener): Bediener {
        val id = dao.insert(bediener.toEntity())
        return bediener.copy(id = id)
    }

    override suspend fun aktualisieren(bediener: Bediener) {
        dao.update(bediener.toEntity())
    }

    override suspend fun deaktivieren(id: Long) {
        dao.deaktivieren(id)
    }

    override suspend fun findById(id: Long): Bediener? =
        dao.findById(id)?.toDomain()

    override suspend fun findByPinHash(pinHash: String): Bediener? =
        dao.findByPinHash(pinHash)?.toDomain()

    override fun beobachteAktive(): Flow<List<Bediener>> =
        dao.beobachteAktive().map { it.map { e -> e.toDomain() } }
}
