package de.barpos.kassensystem.domain.repository

import de.barpos.kassensystem.domain.model.Schicht
import kotlinx.coroutines.flow.Flow

interface ISchichtRepository {

    suspend fun erstellen(schicht: Schicht): Schicht
    suspend fun findById(id: Long): Schicht?
    suspend fun findAktuelleSchicht(): Schicht?
    fun beobachteSchichtenFuerBediener(bedienerId: Long): Flow<List<Schicht>>

    suspend fun schichtSchliessen(
        schichtId: Long,
        istBestandInCent: Long
    )
}
