package de.barpos.kassensystem.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "sku",
    foreignKeys = [
        ForeignKey(
            entity = ArtikelEntity::class,
            parentColumns = ["id"],
            childColumns = ["artikel_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("artikel_id")]
)
data class SkuEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "artikel_id") val artikelId: Long,
    @ColumnInfo(name = "bezeichnung") val bezeichnung: String,
    @ColumnInfo(name = "varianten_attributen_json") val variantenAttributenJson: String = "{}",
    @ColumnInfo(name = "normalpreis_in_cent") val normalpreisInCent: Long,
    @ColumnInfo(name = "mwst_satz") val mwstSatz: Int = 19,
    @ColumnInfo(name = "bestand") val bestand: Int = 0,
    @ColumnInfo(name = "meldebestand") val meldebestand: Int = 5,
    @ColumnInfo(name = "aktiv") val aktiv: Boolean = true
)
