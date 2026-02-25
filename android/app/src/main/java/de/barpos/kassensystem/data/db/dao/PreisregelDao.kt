package de.barpos.kassensystem.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.barpos.kassensystem.data.db.entity.PreisregelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PreisregelDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(preisregel: PreisregelEntity): Long

    @Update
    suspend fun update(preisregel: PreisregelEntity)

    @Query("SELECT * FROM preisregel WHERE id = :id")
    suspend fun findById(id: Long): PreisregelEntity?

    @Query("SELECT * FROM preisregel WHERE aktiv = 1 ORDER BY prioritaet DESC")
    fun beobachteAktive(): Flow<List<PreisregelEntity>>

    @Query("SELECT * FROM preisregel ORDER BY prioritaet DESC")
    fun beobachteAlle(): Flow<List<PreisregelEntity>>

    @Query("UPDATE preisregel SET aktiv = 0 WHERE id = :id")
    suspend fun deaktivieren(id: Long)
}
