package de.barpos.kassensystem.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Immutable DSFinV-K archive shadow record.
 * Created once per locked [TransaktionEntity] during export.
 * Never updated — schema changes do not affect already-exported rows.
 */
@Entity(
    tableName = "dsfinvk_archiv_satz",
    foreignKeys = [
        ForeignKey(
            entity = TransaktionEntity::class,
            parentColumns = ["id"],
            childColumns = ["transaktion_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("transaktion_id")]
)
data class DsFinVKArchivSatzEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "transaktion_id") val transaktionId: Long,
    @ColumnInfo(name = "csv_row_json") val csvRowJson: String,   // DSFinV-K 2.3 fields as JSON
    @ColumnInfo(name = "export_zeit") val exportZeit: Long
)
