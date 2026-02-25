package de.barpos.kassensystem.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.barpos.kassensystem.data.db.entity.TseProtokollEintragEntity

@Dao
interface TseProtokollEintragDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(eintrag: TseProtokollEintragEntity): Long

    @Query("SELECT * FROM tse_protokoll_eintrag WHERE transaktion_id = :transaktionId ORDER BY id ASC")
    suspend fun findByTransaktionId(transaktionId: Long): List<TseProtokollEintragEntity>

    @Query("SELECT MAX(tx_nummer) FROM tse_protokoll_eintrag")
    suspend fun maxTxNummer(): Long?

    @Query("SELECT COUNT(*) FROM tse_protokoll_eintrag WHERE transaktion_id = :transaktionId")
    suspend fun anzahlFuerTransaktion(transaktionId: Long): Int
}
