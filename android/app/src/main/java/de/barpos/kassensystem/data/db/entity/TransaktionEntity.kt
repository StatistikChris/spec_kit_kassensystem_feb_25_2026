package de.barpos.kassensystem.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transaktion",
    indices = [
        Index("tisch_id"),
        Index("bediener_id"),
        Index("schicht_id"),
        Index("status"),
        Index("locked")
    ]
)
data class TransaktionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "uuid") val uuid: String,
    @ColumnInfo(name = "tisch_id") val tischId: Long?,
    @ColumnInfo(name = "bediener_id") val bedienerId: Long,
    @ColumnInfo(name = "schicht_id") val schichtId: Long,
    @ColumnInfo(name = "status") val status: String,            // TransaktionStatus.name
    @ColumnInfo(name = "zahlungsart") val zahlungsart: String,  // Zahlungsart.name
    @ColumnInfo(name = "gesamt_in_cent") val gesamtInCent: Long,
    @ColumnInfo(name = "storno_von_id") val stornoVonId: Long?,
    @ColumnInfo(name = "is_training") val isTraining: Boolean = false,
    @ColumnInfo(name = "locked") val locked: Boolean = false,
    @ColumnInfo(name = "created_at") val createdAt: Long,       // Instant → epochMillis
    @ColumnInfo(name = "updated_at") val updatedAt: Long
)
