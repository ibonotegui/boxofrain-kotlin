package io.github.ibonotegui.boxofrain.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Location(
    @PrimaryKey val name: String,
    @ColumnInfo val latitude: String,
    @ColumnInfo val longitude: String,
    @ColumnInfo var isDefault: Boolean = false
)
//@ColumnInfo(name = "longitude") val longitude: String
