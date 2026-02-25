package de.barpos.kassensystem.data.repository

import de.barpos.kassensystem.data.db.dao.TischDao
import de.barpos.kassensystem.data.mapper.toDomain
import de.barpos.kassensystem.data.mapper.toEntity
import de.barpos.kassensystem.domain.model.Tisch
import de.barpos.kassensystem.domain.repository.ITischRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.threeten.bp.Instant
import javax.inject.Inject

class TischRepositoryImpl @Inject constructor(
    private val tischDao: TischDao
) : ITischRepository {

    override suspend fun erstellen(tisch: Tisch): Tisch {
        val id = tischDao.insert(tisch.toEntity())
        return tisch.copy(id = id)
    }

    override suspend fun findById(id: Long): Tisch? =
        tischDao.findById(id)?.toDomain()

    override fun beobachteAlle(): Flow<List<Tisch>> =
        tischDao.beobachteAlle().map { it.map { e -> e.toDomain() } }

    override fun beobachteBesetzt(): Flow<List<Tisch>> =
        tischDao.beobachteBesetzt().map { it.map { e -> e.toDomain() } }

    override suspend fun statusAktualisieren(
        tischId: Long,
        neuerStatus: String,
        erwartetVersion: Long
    ): Boolean {
        val jetzt = Instant.now().toEpochMilli()
        val updated = tischDao.statusAktualisierenOptimistisch(tischId, neuerStatus, erwartetVersion, jetzt)
        return updated > 0
    }
}
