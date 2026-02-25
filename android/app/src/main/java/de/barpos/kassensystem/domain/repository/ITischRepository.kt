package de.barpos.kassensystem.domain.repository

import de.barpos.kassensystem.domain.model.Tisch
import kotlinx.coroutines.flow.Flow

interface ITischRepository {

    suspend fun erstellen(tisch: Tisch): Tisch
    suspend fun findById(id: Long): Tisch?
    fun beobachteAlle(): Flow<List<Tisch>>
    fun beobachteBesetzt(): Flow<List<Tisch>>

    /** Optimistic-lock update; returns false if version mismatch. */
    suspend fun statusAktualisieren(tischId: Long, neuerStatus: String, erwartetVersion: Long): Boolean
}
