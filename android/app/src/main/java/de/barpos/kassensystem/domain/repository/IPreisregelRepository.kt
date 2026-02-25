package de.barpos.kassensystem.domain.repository

import de.barpos.kassensystem.domain.model.Preisregel
import kotlinx.coroutines.flow.Flow

interface IPreisregelRepository {

    suspend fun erstellen(preisregel: Preisregel): Preisregel
    suspend fun aktualisieren(preisregel: Preisregel)
    suspend fun deaktivieren(id: Long)
    suspend fun findById(id: Long): Preisregel?
    fun beobachteAktive(): Flow<List<Preisregel>>
    fun beobachteAlle(): Flow<List<Preisregel>>
}
