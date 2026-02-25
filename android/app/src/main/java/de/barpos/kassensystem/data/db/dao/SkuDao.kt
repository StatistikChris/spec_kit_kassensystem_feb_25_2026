package de.barpos.kassensystem.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.barpos.kassensystem.data.db.entity.SkuEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SkuDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(sku: SkuEntity): Long

    @Update
    suspend fun update(sku: SkuEntity)

    @Query("SELECT * FROM sku WHERE id = :id")
    suspend fun findById(id: Long): SkuEntity?

    @Query("SELECT * FROM sku WHERE artikel_id = :artikelId AND aktiv = 1 ORDER BY bezeichnung ASC")
    suspend fun findByArtikelId(artikelId: Long): List<SkuEntity>

    @Query("SELECT * FROM sku WHERE aktiv = 1 ORDER BY bezeichnung ASC")
    fun beobachteAktive(): Flow<List<SkuEntity>>

    @Query("SELECT * FROM sku WHERE bestand <= meldebestand AND aktiv = 1")
    fun beobachteMeldebestands(): Flow<List<SkuEntity>>

    @Query("UPDATE sku SET bestand = bestand - :menge WHERE id = :id AND bestand >= :menge")
    suspend fun bestandVerringern(id: Long, menge: Int): Int

    @Query("UPDATE sku SET bestand = bestand + :menge WHERE id = :id")
    suspend fun bestandErhoehen(id: Long, menge: Int)
}
