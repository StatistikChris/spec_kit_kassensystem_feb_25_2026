package de.barpos.kassensystem.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.barpos.kassensystem.data.db.entity.VerfahrensdokumentationEntity

@Dao
interface VerfahrensdokumentationDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(eintrag: VerfahrensdokumentationEntity): Long

    @Query("SELECT * FROM verfahrensdokumentation ORDER BY zeitstempel DESC")
    suspend fun alleEintraege(): List<VerfahrensdokumentationEntity>

    @Query("SELECT * FROM verfahrensdokumentation ORDER BY zeitstempel DESC LIMIT 1")
    suspend fun letzterEintrag(): VerfahrensdokumentationEntity?
}
