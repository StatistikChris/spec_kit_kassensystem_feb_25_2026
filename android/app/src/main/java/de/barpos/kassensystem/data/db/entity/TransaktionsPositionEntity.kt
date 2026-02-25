package de.barpos.kassensystem.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transaktion_position",
    foreignKeys = [
        ForeignKey(
            entity = TransaktionEntity::class,
            parentColumns = ["id"],
            childColumns = ["transaktion_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("transaktion_id"), Index("sku_id")]
)
data class TransaktionsPositionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "transaktion_id") val transaktionId: Long,
    @ColumnInfo(name = "sku_id") val skuId: Long,
    @ColumnInfo(name = "sku_bezeichnung") val skuBezeichnung: String,
    @ColumnInfo(name = "menge") val menge: Int,
    @ColumnInfo(name = "einzelpreis_in_cent") val einzelpreisInCent: Long,
    @ColumnInfo(name = "gesamtpreis_in_cent") val gesamtpreisInCent: Long,
    @ColumnInfo(name = "mwst_satz") val mwstSatz: Int,
    @ColumnInfo(name = "mwst_betrag_in_cent") val mwstBetragInCent: Long,
    @ColumnInfo(name = "preisregel_id") val preisregelId: Long?,
    @ColumnInfo(name = "created_at") val createdAt: Long
)
