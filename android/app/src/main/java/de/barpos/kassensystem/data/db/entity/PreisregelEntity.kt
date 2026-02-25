package de.barpos.kassensystem.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preisregel")
data class PreisregelEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "bezeichnung") val bezeichnung: String,
    @ColumnInfo(name = "start_zeit") val startZeit: String,     // ISO LocalTime "HH:mm:ss"
    @ColumnInfo(name = "end_zeit") val endZeit: String,
    @ColumnInfo(name = "weekday_bitmask") val weekdayBitmask: Int = 0b1111111,
    @ColumnInfo(name = "rabatt_typ") val rabattTyp: String,     // RabattTyp.name
    @ColumnInfo(name = "rabatt_wert") val rabattWert: Long,
    @ColumnInfo(name = "artikel_filter") val artikelFilter: String = "ALLE",
    @ColumnInfo(name = "filter_wert") val filterWert: String?,
    @ColumnInfo(name = "prioritaet") val prioritaet: Int = 0,
    @ColumnInfo(name = "aktiv") val aktiv: Boolean = true,
    @ColumnInfo(name = "created_at") val createdAt: Long
)
