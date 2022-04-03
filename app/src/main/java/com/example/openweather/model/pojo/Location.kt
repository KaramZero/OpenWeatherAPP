package com.example.openweather.model.pojo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class Location(
    @ColumnInfo(name = "lat") var lat: Double = -34.0,
    @ColumnInfo(name = "long") var long: Double = 151.0,
    @PrimaryKey @ColumnInfo(name = "city") var city: String = "sydney"
)
