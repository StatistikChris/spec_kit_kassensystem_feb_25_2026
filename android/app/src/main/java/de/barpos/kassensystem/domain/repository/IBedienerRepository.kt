package de.barpos.kassensystem.domain.repository

import de.barpos.kassensystem.domain.model.Bediener
import kotlinx.coroutines.flow.Flow

interface IBedienerRepository {

    suspend fun erstellen(bediener: Bediener): Bediener
    suspend fun aktualisieren(bediener: Bediener)
    suspend fun deaktivieren(id: Long)
    suspend fun findById(id: Long): Bediener?
    suspend fun findByPinHash(pinHash: String): Bediener?
    fun beobachteAktive(): Flow<List<Bediener>>
}
