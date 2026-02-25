package de.barpos.kassensystem.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ausfallzeit_eintrag")
data class AusfallzeitEintragEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "start_zeit") val startZeit: Long,
    @ColumnInfo(name = "end_zeit") val endZeit: Long?,
    @ColumnInfo(name = "dauer_sekunden") val dauerSekunden: Long?,
    @ColumnInfo(name = "ursache") val ursache: String = "Systemausfall / unbekannt",
    @ColumnInfo(name = "bediener_id") val bedienerId: Long?,
    @ColumnInfo(name = "created_at") val createdAt: Long
)
