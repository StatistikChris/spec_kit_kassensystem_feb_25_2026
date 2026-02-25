package de.barpos.kassensystem.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.barpos.kassensystem.data.db.entity.SchichtEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SchichtDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(schicht: SchichtEntity): Long

    @Update
    suspend fun update(schicht: SchichtEntity)

    @Query("SELECT * FROM schicht WHERE id = :id")
    suspend fun findById(id: Long): SchichtEntity?

    @Query("SELECT * FROM schicht WHERE geschlossen = 0 ORDER BY start_zeit DESC LIMIT 1")
    suspend fun findAktuelleSchicht(): SchichtEntity?

    @Query("SELECT * FROM schicht WHERE bediener_id = :bedienerId ORDER BY start_zeit DESC")
    fun beobachteSchichtenFuerBediener(bedienerId: Long): Flow<List<SchichtEntity>>

    @Query("UPDATE schicht SET geschlossen = 1, end_zeit = :endZeit, ist_bestand_in_cent = :istBestandInCent WHERE id = :id")
    suspend fun schichtSchliessen(id: Long, endZeit: Long, istBestandInCent: Long)
}
