package de.barpos.kassensystem.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.barpos.kassensystem.data.db.entity.AusfallzeitEintragEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AusfallzeitEintragDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(eintrag: AusfallzeitEintragEntity): Long

    @Update
    suspend fun update(eintrag: AusfallzeitEintragEntity)

    @Query("SELECT * FROM ausfallzeit_eintrag WHERE id = :id")
    suspend fun findById(id: Long): AusfallzeitEintragEntity?

    /** Returns the open (end_zeit IS NULL) crash window, if any. */
    @Query("SELECT * FROM ausfallzeit_eintrag WHERE end_zeit IS NULL ORDER BY start_zeit DESC LIMIT 1")
    suspend fun findOffenerAusfall(): AusfallzeitEintragEntity?

    @Query("SELECT * FROM ausfallzeit_eintrag ORDER BY start_zeit DESC")
    fun beobachteAlle(): Flow<List<AusfallzeitEintragEntity>>

    @Query("SELECT * FROM ausfallzeit_eintrag WHERE start_zeit BETWEEN :von AND :bis ORDER BY start_zeit ASC")
    suspend fun findInZeitraum(von: Long, bis: Long): List<AusfallzeitEintragEntity>
}
