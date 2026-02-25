package de.barpos.kassensystem.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.barpos.kassensystem.data.db.entity.BonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BonDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(bon: BonEntity): Long

    @Query("SELECT * FROM bon WHERE id = :id")
    suspend fun findById(id: Long): BonEntity?

    @Query("SELECT * FROM bon WHERE transaktion_id = :transaktionId LIMIT 1")
    suspend fun findByTransaktionId(transaktionId: Long): BonEntity?

    @Query("SELECT * FROM bon ORDER BY zeitstempel DESC")
    fun beobachteAlle(): Flow<List<BonEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM bon WHERE transaktion_id = :transaktionId)")
    suspend fun existsFuerTransaktion(transaktionId: Long): Boolean
}
