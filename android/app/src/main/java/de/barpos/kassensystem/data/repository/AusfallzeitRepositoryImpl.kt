package de.barpos.kassensystem.data.repository

import de.barpos.kassensystem.data.db.dao.AusfallzeitEintragDao
import de.barpos.kassensystem.data.db.entity.AusfallzeitEintragEntity
import de.barpos.kassensystem.data.mapper.toDomain
import de.barpos.kassensystem.domain.model.AusfallzeitEintrag
import de.barpos.kassensystem.domain.repository.IAusfallzeitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.threeten.bp.Instant
import javax.inject.Inject

class AusfallzeitRepositoryImpl @Inject constructor(
    private val dao: AusfallzeitEintragDao
) : IAusfallzeitRepository {

    override suspend fun ausfallBeginnErfassen(): Long {
        val jetzt = Instant.now().toEpochMilli()
        val entity = AusfallzeitEintragEntity(
            startZeit = jetzt,
            endZeit = null,
            dauerSekunden = null,
            ursache = "Systemausfall / unbekannt",
            bedienerId = null,
            createdAt = jetzt
        )
        return dao.insert(entity)
    }

    override suspend fun ausfallEndeErfassen(id: Long, ursache: String) {
        val existing = dao.findById(id) ?: return
        val jetzt = Instant.now().toEpochMilli()
        val dauer = (jetzt - existing.startZeit) / 1000
        dao.update(existing.copy(endZeit = jetzt, dauerSekunden = dauer, ursache = ursache))
    }

    override suspend fun findOffenerAusfall(): AusfallzeitEintrag? =
        dao.findOffenerAusfall()?.toDomain()

    override fun beobachteAlle(): Flow<List<AusfallzeitEintrag>> =
        dao.beobachteAlle().map { it.map { e -> e.toDomain() } }

    override suspend fun findInZeitraum(vonMillis: Long, bisMillis: Long): List<AusfallzeitEintrag> =
        dao.findInZeitraum(vonMillis, bisMillis).map { it.toDomain() }
}
