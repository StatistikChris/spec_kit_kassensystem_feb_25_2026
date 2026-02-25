package de.barpos.kassensystem.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bediener")
data class BedienerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "kurzname") val kurzname: String,
    @ColumnInfo(name = "pin_hash") val pinHash: String,
    @ColumnInfo(name = "rolle") val rolle: String,              // BedienerRolle.name
    @ColumnInfo(name = "is_training") val isTraining: Boolean = false,
    @ColumnInfo(name = "aktiv") val aktiv: Boolean = true
)
