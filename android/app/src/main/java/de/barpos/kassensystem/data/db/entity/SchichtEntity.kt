package de.barpos.kassensystem.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "schicht",
    foreignKeys = [
        ForeignKey(
            entity = BedienerEntity::class,
            parentColumns = ["id"],
            childColumns = ["bediener_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("bediener_id")]
)
data class SchichtEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "bediener_id") val bedienerId: Long,
    @ColumnInfo(name = "start_zeit") val startZeit: Long,
    @ColumnInfo(name = "end_zeit") val endZeit: Long?,
    @ColumnInfo(name = "soll_bestand_in_cent") val sollBestandInCent: Long?,
    @ColumnInfo(name = "ist_bestand_in_cent") val istBestandInCent: Long?,
    @ColumnInfo(name = "geschlossen") val geschlossen: Boolean = false
)
