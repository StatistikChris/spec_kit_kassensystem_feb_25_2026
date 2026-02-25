package de.barpos.kassensystem.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import de.barpos.kassensystem.data.db.entity.TransaktionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransaktionDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(transaktion: TransaktionEntity): Long

    @Update
    suspend fun update(transaktion: TransaktionEntity)

    @Query("SELECT * FROM transaktion WHERE id = :id")
    suspend fun findById(id: Long): TransaktionEntity?

    @Query("SELECT * FROM transaktion WHERE uuid = :uuid LIMIT 1")
    suspend fun findByUuid(uuid: String): TransaktionEntity?

    @Query("SELECT * FROM transaktion WHERE tisch_id = :tischId AND status = 'OFFEN' LIMIT 1")
    suspend fun findOffeneTransaktionByTisch(tischId: Long): TransaktionEntity?

    @Query("SELECT * FROM transaktion WHERE schicht_id = :schichtId ORDER BY created_at DESC")
    fun beobachteTransaktionenFuerSchicht(schichtId: Long): Flow<List<TransaktionEntity>>

    @Query("SELECT * FROM transaktion WHERE status = 'OFFEN' ORDER BY created_at DESC")
    fun beobachteOffeneTransaktionen(): Flow<List<TransaktionEntity>>

    @Query("UPDATE transaktion SET locked = 1 WHERE id = :id")
    suspend fun lock(id: Long)

    @Query("SELECT * FROM transaktion WHERE locked = 0 ORDER BY created_at DESC")
    suspend fun alleUngesperrten(): List<TransaktionEntity>

    @Query("SELECT COUNT(*) FROM transaktion WHERE schicht_id = :schichtId AND status = 'ABGESCHLOSSEN'")
    suspend fun anzahlAbgeschlossenerFuerSchicht(schichtId: Long): Int

    @Query(
        """SELECT SUM(gesamt_in_cent) FROM transaktion 
           WHERE schicht_id = :schichtId 
           AND status = 'ABGESCHLOSSEN' 
           AND is_training = 0"""
    )
    suspend fun summeGesamtFuerSchicht(schichtId: Long): Long?
}
