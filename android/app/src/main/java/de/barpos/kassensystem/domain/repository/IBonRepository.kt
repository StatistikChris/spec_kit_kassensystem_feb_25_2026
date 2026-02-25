package de.barpos.kassensystem.domain.repository

import de.barpos.kassensystem.domain.model.Bon
import de.barpos.kassensystem.domain.model.ZBon
import kotlinx.coroutines.flow.Flow

interface IBonRepository {

    suspend fun bonSpeichern(bon: Bon): Bon
    suspend fun findBonByTransaktionId(transaktionId: Long): Bon?
    suspend fun bonExistiertFuerTransaktion(transaktionId: Long): Boolean
    fun beobachteBons(): Flow<List<Bon>>

    suspend fun zBonErstellen(zBon: ZBon): ZBon
    suspend fun findZBonById(id: Long): ZBon?
    suspend fun naechsterNullstellungszaehler(): Long
    fun beobachteZBons(): Flow<List<ZBon>>
    suspend fun zBonsInZeitraum(vonMillis: Long, bisMillis: Long): List<ZBon>
}
