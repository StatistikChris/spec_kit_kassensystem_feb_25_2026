package de.barpos.kassensystem.data.repository

import de.barpos.kassensystem.data.db.dao.SchichtDao
import de.barpos.kassensystem.data.mapper.toDomain
import de.barpos.kassensystem.data.mapper.toEntity
import de.barpos.kassensystem.domain.model.Schicht
import de.barpos.kassensystem.domain.repository.ISchichtRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.threeten.bp.Instant
import javax.inject.Inject

class SchichtRepositoryImpl @Inject constructor(
    private val dao: SchichtDao
) : ISchichtRepository {

    override suspend fun erstellen(schicht: Schicht): Schicht {
        val id = dao.insert(schicht.toEntity())
        return schicht.copy(id = id)
    }

    override suspend fun findById(id: Long): Schicht? =
        dao.findById(id)?.toDomain()

    override suspend fun findAktuelleSchicht(): Schicht? =
        dao.findAktuelleSchicht()?.toDomain()

    override fun beobachteSchichtenFuerBediener(bedienerId: Long): Flow<List<Schicht>> =
        dao.beobachteSchichtenFuerBediener(bedienerId).map { it.map { e -> e.toDomain() } }

    override suspend fun schichtSchliessen(schichtId: Long, istBestandInCent: Long) {
        val jetzt = Instant.now().toEpochMilli()
        dao.schichtSchliessen(schichtId, jetzt, istBestandInCent)
    }
}
