package de.barpos.kassensystem.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.barpos.kassensystem.data.db.entity.BedienerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BedienerDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(bediener: BedienerEntity): Long

    @Update
    suspend fun update(bediener: BedienerEntity)

    @Query("SELECT * FROM bediener WHERE id = :id")
    suspend fun findById(id: Long): BedienerEntity?

    @Query("SELECT * FROM bediener WHERE aktiv = 1 ORDER BY name ASC")
    fun beobachteAktive(): Flow<List<BedienerEntity>>

    @Query("SELECT * FROM bediener WHERE pin_hash = :pinHash AND aktiv = 1 LIMIT 1")
    suspend fun findByPinHash(pinHash: String): BedienerEntity?

    @Query("UPDATE bediener SET aktiv = 0 WHERE id = :id")
    suspend fun deaktivieren(id: Long)
}
