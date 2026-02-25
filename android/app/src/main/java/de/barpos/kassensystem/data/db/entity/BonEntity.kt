package de.barpos.kassensystem.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "bon",
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
data class BonEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "transaktion_id") val transaktionId: Long,
    @ColumnInfo(name = "zeitstempel") val zeitstempel: Long,
    @ColumnInfo(name = "tse_signatur") val tseSignatur: String,
    @ColumnInfo(name = "tse_serial_number") val tseSerialNumber: String,
    @ColumnInfo(name = "tse_zeitpunkt_start") val tseZeitpunktStart: Long,
    @ColumnInfo(name = "tse_zeitpunkt_end") val tseZeitpunktEnd: Long,
    @ColumnInfo(name = "tse_tx_nummer") val tseTxNummer: Long,
    @ColumnInfo(name = "is_nachdruck") val isNachdruck: Boolean = false,
    @ColumnInfo(name = "nachdruck_zeit") val nachdruckZeit: Long?
)
