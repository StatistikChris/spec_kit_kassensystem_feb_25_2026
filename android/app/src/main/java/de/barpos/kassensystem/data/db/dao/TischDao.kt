package de.barpos.kassensystem.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.barpos.kassensystem.data.db.entity.TischEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TischDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(tisch: TischEntity): Long

    @Update
    suspend fun update(tisch: TischEntity)

    @Query("SELECT * FROM tisch WHERE id = :id")
    suspend fun findById(id: Long): TischEntity?

    @Query("SELECT * FROM tisch ORDER BY bezeichnung ASC")
    fun beobachteAlle(): Flow<List<TischEntity>>

    @Query("SELECT * FROM tisch WHERE status = 'BESETZT' ORDER BY bezeichnung ASC")
    fun beobachteBesetzt(): Flow<List<TischEntity>>

    @Query(
        """UPDATE tisch SET status = :neuerStatus, version = version + 1, updated_at = :jetzt
           WHERE id = :id AND version = :erwartetVersion"""
    )
    suspend fun statusAktualisierenOptimistisch(
        id: Long,
        neuerStatus: String,
        erwartetVersion: Long,
        jetzt: Long
    ): Int
}
