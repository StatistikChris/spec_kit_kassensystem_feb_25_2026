package de.barpos.kassensystem.domain.repository

import de.barpos.kassensystem.domain.model.Transaktion
import de.barpos.kassensystem.domain.model.TransaktionsPosition
import kotlinx.coroutines.flow.Flow

interface ITransaktionRepository {

    suspend fun erstellen(transaktion: Transaktion): Transaktion
    suspend fun aktualisieren(transaktion: Transaktion)
    suspend fun findById(id: Long): Transaktion?
    suspend fun findByUuid(uuid: String): Transaktion?
    suspend fun findOffeneTransaktionByTisch(tischId: Long): Transaktion?
    suspend fun sperren(id: Long)

    fun beobachteOffene(): Flow<List<Transaktion>>
    fun beobachteFuerSchicht(schichtId: Long): Flow<List<Transaktion>>

    suspend fun positionenFuerTransaktion(transaktionId: Long): List<TransaktionsPosition>
    fun beobachtePositionen(transaktionId: Long): Flow<List<TransaktionsPosition>>

    suspend fun positionHinzufuegen(position: TransaktionsPosition): TransaktionsPosition
    suspend fun positionEntfernen(positionId: Long)
    suspend fun allePositionenEntfernen(transaktionId: Long)
}
