package de.barpos.kassensystem.data.repository

import de.barpos.kassensystem.data.db.dao.BonDao
import de.barpos.kassensystem.data.db.dao.ZBonDao
import de.barpos.kassensystem.data.mapper.toDomain
import de.barpos.kassensystem.data.mapper.toEntity
import de.barpos.kassensystem.domain.model.Bon
import de.barpos.kassensystem.domain.model.ZBon
import de.barpos.kassensystem.domain.repository.IBonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BonRepositoryImpl @Inject constructor(
    private val bonDao: BonDao,
    private val zBonDao: ZBonDao
) : IBonRepository {

    override suspend fun bonSpeichern(bon: Bon): Bon {
        val id = bonDao.insert(bon.toEntity())
        return bon.copy(id = id)
    }

    override suspend fun findBonByTransaktionId(transaktionId: Long): Bon? =
        bonDao.findByTransaktionId(transaktionId)?.toDomain()

    override suspend fun bonExistiertFuerTransaktion(transaktionId: Long): Boolean =
        bonDao.existsFuerTransaktion(transaktionId)

    override fun beobachteBons(): Flow<List<Bon>> =
        bonDao.beobachteAlle().map { it.map { e -> e.toDomain() } }

    override suspend fun zBonErstellen(zBon: ZBon): ZBon {
        val id = zBonDao.insert(zBon.toEntity())
        return zBon.copy(id = id)
    }

    override suspend fun findZBonById(id: Long): ZBon? =
        zBonDao.findById(id)?.toDomain()

    override suspend fun naechsterNullstellungszaehler(): Long =
        (zBonDao.maxNullstellungszaehler() ?: 0L) + 1L

    override fun beobachteZBons(): Flow<List<ZBon>> =
        zBonDao.beobachteAlle().map { it.map { e -> e.toDomain() } }

    override suspend fun zBonsInZeitraum(vonMillis: Long, bisMillis: Long): List<ZBon> =
        zBonDao.findInZeitraum(vonMillis, bisMillis).map { it.toDomain() }
}
