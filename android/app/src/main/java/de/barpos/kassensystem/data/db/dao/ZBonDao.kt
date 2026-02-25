package de.barpos.kassensystem.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.barpos.kassensystem.data.db.entity.ZBonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ZBonDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(zBon: ZBonEntity): Long

    @Query("SELECT * FROM z_bon WHERE id = :id")
    suspend fun findById(id: Long): ZBonEntity?

    @Query("SELECT MAX(nullstellungszaehler) FROM z_bon")
    suspend fun maxNullstellungszaehler(): Long?

    @Query("SELECT * FROM z_bon ORDER BY created_at DESC")
    fun beobachteAlle(): Flow<List<ZBonEntity>>

    @Query("SELECT * FROM z_bon WHERE created_at BETWEEN :von AND :bis ORDER BY created_at ASC")
    suspend fun findInZeitraum(von: Long, bis: Long): List<ZBonEntity>
}
