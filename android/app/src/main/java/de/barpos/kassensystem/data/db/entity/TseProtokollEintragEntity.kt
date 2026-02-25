package de.barpos.kassensystem.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tse_protokoll_eintrag",
    foreignKeys = [
        ForeignKey(
            entity = TransaktionEntity::class,
            parentColumns = ["id"],
            childColumns = ["transaktion_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("transaktion_id"), Index("tx_nummer")]
)
data class TseProtokollEintragEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "transaktion_id") val transaktionId: Long,
    @ColumnInfo(name = "typ") val typ: String,                       // TseVorgangTyp.name
    @ColumnInfo(name = "tse_serial_number") val tseSerialNumber: String,
    @ColumnInfo(name = "signatur") val signatur: String,
    @ColumnInfo(name = "zeitpunkt_start") val zeitpunktStart: Long,
    @ColumnInfo(name = "zeitpunkt_end") val zeitpunktEnd: Long,
    @ColumnInfo(name = "tx_nummer") val txNummer: Long,
    @ColumnInfo(name = "signatur_zaehler") val signaturZaehler: Long,
    @ColumnInfo(name = "vorgangs_daten") val vorgangsDaten: String
)
