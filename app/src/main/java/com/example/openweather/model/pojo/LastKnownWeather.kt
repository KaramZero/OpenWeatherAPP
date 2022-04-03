package com.example.openweather.model.pojo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lastWeather")
data class LastKnownWeather(
    @PrimaryKey @ColumnInfo(name = "city") var city: String = "sydney",
    @ColumnInfo(name = "data") var data: String = ""
)
