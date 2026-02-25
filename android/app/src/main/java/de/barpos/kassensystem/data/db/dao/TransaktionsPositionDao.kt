package de.barpos.kassensystem.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.barpos.kassensystem.data.db.entity.TransaktionsPositionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransaktionsPositionDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(position: TransaktionsPositionEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(positionen: List<TransaktionsPositionEntity>)

    @Query("SELECT * FROM transaktion_position WHERE transaktion_id = :transaktionId ORDER BY id ASC")
    suspend fun findByTransaktionId(transaktionId: Long): List<TransaktionsPositionEntity>

    @Query("SELECT * FROM transaktion_position WHERE transaktion_id = :transaktionId ORDER BY id ASC")
    fun beobachtePositionenFuerTransaktion(transaktionId: Long): Flow<List<TransaktionsPositionEntity>>

    @Query("DELETE FROM transaktion_position WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM transaktion_position WHERE transaktion_id = :transaktionId")
    suspend fun deleteAllForTransaktion(transaktionId: Long)
}
