package de.barpos.kassensystem.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.barpos.kassensystem.data.db.entity.ArtikelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtikelDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(artikel: ArtikelEntity): Long

    @Update
    suspend fun update(artikel: ArtikelEntity)

    @Query("SELECT * FROM artikel WHERE id = :id")
    suspend fun findById(id: Long): ArtikelEntity?

    @Query("SELECT * FROM artikel WHERE aktiv = 1 ORDER BY name ASC")
    fun beobachteAktive(): Flow<List<ArtikelEntity>>

    @Query("SELECT * FROM artikel ORDER BY name ASC")
    fun beobachteAlle(): Flow<List<ArtikelEntity>>

    @Query("UPDATE artikel SET aktiv = 0 WHERE id = :id")
    suspend fun deaktivieren(id: Long)
}
