package de.barpos.kassensystem.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "z_bon")
data class ZBonEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "nummer") val nummer: Long,
    @ColumnInfo(name = "von_zeit") val vonZeit: Long,
    @ColumnInfo(name = "bis_zeit") val bisZeit: Long,
    @ColumnInfo(name = "gesamt_in_cent") val gesamtInCent: Long,
    @ColumnInfo(name = "bar_in_cent") val barInCent: Long,
    @ColumnInfo(name = "extern_in_cent") val externInCent: Long,
    @ColumnInfo(name = "mwst7_in_cent") val mwst7InCent: Long,
    @ColumnInfo(name = "mwst19_in_cent") val mwst19InCent: Long,
    @ColumnInfo(name = "entnahmen_in_cent") val entnahmenInCent: Long = 0L,
    @ColumnInfo(name = "anzahl_buchungen") val anzahlBuchungen: Int,
    @ColumnInfo(name = "anzahl_stornos") val anzahlStornos: Int,
    @ColumnInfo(name = "trainee_umsatz_in_cent") val traineeUmsatzInCent: Long = 0L,
    @ColumnInfo(name = "nullstellungszaehler") val nullstellungszaehler: Long,
    @ColumnInfo(name = "bediener_id") val bedienerId: Long,
    @ColumnInfo(name = "tse_signatur") val tseSignatur: String,
    @ColumnInfo(name = "tse_serial_number") val tseSerialNumber: String,
    @ColumnInfo(name = "created_at") val createdAt: Long
)
