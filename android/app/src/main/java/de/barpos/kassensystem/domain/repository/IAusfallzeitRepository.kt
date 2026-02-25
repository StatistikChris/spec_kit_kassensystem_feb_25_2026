package de.barpos.kassensystem.domain.repository

import de.barpos.kassensystem.domain.model.AusfallzeitEintrag
import kotlinx.coroutines.flow.Flow

interface IAusfallzeitRepository {

    /** Called at app startup — opens a crash window if the last run exited dirty. */
    suspend fun ausfallBeginnErfassen(): Long

    /** Called when the crash window has been confirmed by the user. */
    suspend fun ausfallEndeErfassen(id: Long, ursache: String)

    suspend fun findOffenerAusfall(): AusfallzeitEintrag?
    fun beobachteAlle(): Flow<List<AusfallzeitEintrag>>
    suspend fun findInZeitraum(vonMillis: Long, bisMillis: Long): List<AusfallzeitEintrag>
}
