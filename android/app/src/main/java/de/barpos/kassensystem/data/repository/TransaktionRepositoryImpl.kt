package de.barpos.kassensystem.data.repository

import de.barpos.kassensystem.data.db.dao.TransaktionDao
import de.barpos.kassensystem.data.db.dao.TransaktionsPositionDao
import de.barpos.kassensystem.data.db.entity.TransaktionEntity
import de.barpos.kassensystem.data.db.entity.TransaktionsPositionEntity
import de.barpos.kassensystem.data.mapper.toEntity
import de.barpos.kassensystem.data.mapper.toDomain
import de.barpos.kassensystem.domain.model.Transaktion
import de.barpos.kassensystem.domain.model.TransaktionsPosition
import de.barpos.kassensystem.domain.repository.ITransaktionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransaktionRepositoryImpl @Inject constructor(
    private val transaktionDao: TransaktionDao,
    private val positionDao: TransaktionsPositionDao
) : ITransaktionRepository {

    override suspend fun erstellen(transaktion: Transaktion): Transaktion {
        val id = transaktionDao.insert(transaktion.toEntity())
        return transaktion.copy(id = id)
    }

    override suspend fun aktualisieren(transaktion: Transaktion) {
        transaktionDao.update(transaktion.toEntity())
    }

    override suspend fun findById(id: Long): Transaktion? =
        transaktionDao.findById(id)?.toDomain()

    override suspend fun findByUuid(uuid: String): Transaktion? =
        transaktionDao.findByUuid(uuid)?.toDomain()

    override suspend fun findOffeneTransaktionByTisch(tischId: Long): Transaktion? =
        transaktionDao.findOffeneTransaktionByTisch(tischId)?.toDomain()

    override suspend fun sperren(id: Long) {
        transaktionDao.lock(id)
    }

    override fun beobachteOffene(): Flow<List<Transaktion>> =
        transaktionDao.beobachteOffeneTransaktionen().map { list -> list.map { it.toDomain() } }

    override fun beobachteFuerSchicht(schichtId: Long): Flow<List<Transaktion>> =
        transaktionDao.beobachteTransaktionenFuerSchicht(schichtId)
            .map { list -> list.map { it.toDomain() } }

    override suspend fun positionenFuerTransaktion(transaktionId: Long): List<TransaktionsPosition> =
        positionDao.findByTransaktionId(transaktionId).map { it.toDomain() }

    override fun beobachtePositionen(transaktionId: Long): Flow<List<TransaktionsPosition>> =
        positionDao.beobachtePositionenFuerTransaktion(transaktionId)
            .map { list -> list.map { it.toDomain() } }

    override suspend fun positionHinzufuegen(position: TransaktionsPosition): TransaktionsPosition {
        val id = positionDao.insert(position.toEntity())
        return position.copy(id = id)
    }

    override suspend fun positionEntfernen(positionId: Long) {
        positionDao.deleteById(positionId)
    }

    override suspend fun allePositionenEntfernen(transaktionId: Long) {
        positionDao.deleteAllForTransaktion(transaktionId)
    }
}
