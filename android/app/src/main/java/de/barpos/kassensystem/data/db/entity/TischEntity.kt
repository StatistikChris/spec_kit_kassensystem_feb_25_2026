package de.barpos.kassensystem.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tisch")
data class TischEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "bezeichnung") val bezeichnung: String,
    @ColumnInfo(name = "status") val status: String = "FREI",   // TischStatus.name
    @ColumnInfo(name = "version") val version: Long = 0,
    @ColumnInfo(name = "updated_at") val updatedAt: Long
)
