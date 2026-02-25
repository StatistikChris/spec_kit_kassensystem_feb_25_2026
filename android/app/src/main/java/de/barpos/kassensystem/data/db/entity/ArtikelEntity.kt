package de.barpos.kassensystem.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artikel")
data class ArtikelEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "kategorie") val kategorie: String,
    @ColumnInfo(name = "mwst_satz") val mwstSatz: Int = 19,
    @ColumnInfo(name = "aktiv") val aktiv: Boolean = true
)
