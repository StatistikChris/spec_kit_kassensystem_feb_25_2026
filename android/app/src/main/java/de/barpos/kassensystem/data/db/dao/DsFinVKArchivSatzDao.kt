package de.barpos.kassensystem.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.barpos.kassensystem.data.db.entity.DsFinVKArchivSatzEntity

@Dao
interface DsFinVKArchivSatzDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(satz: DsFinVKArchivSatzEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(saetze: List<DsFinVKArchivSatzEntity>)

    @Query("SELECT * FROM dsfinvk_archiv_satz WHERE transaktion_id = :transaktionId LIMIT 1")
    suspend fun findByTransaktionId(transaktionId: Long): DsFinVKArchivSatzEntity?

    @Query("SELECT * FROM dsfinvk_archiv_satz WHERE export_zeit BETWEEN :von AND :bis ORDER BY export_zeit ASC")
    suspend fun findInZeitraum(von: Long, bis: Long): List<DsFinVKArchivSatzEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM dsfinvk_archiv_satz WHERE transaktion_id = :transaktionId)")
    suspend fun existsFuerTransaktion(transaktionId: Long): Boolean
}
