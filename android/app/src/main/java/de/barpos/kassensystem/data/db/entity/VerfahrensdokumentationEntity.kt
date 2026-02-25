package de.barpos.kassensystem.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "verfahrensdokumentation")
data class VerfahrensdokumentationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "version") val version: String,
    @ColumnInfo(name = "inhalt_hash") val inhaltHash: String,    // SHA-256 of PDF content
    @ColumnInfo(name = "zeitstempel") val zeitstempel: Long,
    @ColumnInfo(name = "aenderung_beschreibung") val aenderungBeschreibung: String = ""
)
