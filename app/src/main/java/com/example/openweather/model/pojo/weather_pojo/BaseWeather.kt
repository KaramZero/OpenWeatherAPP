package com.example.openweather.model.pojo.weather_pojo

import androidx.room.Ignore
import com.google.gson.annotations.SerializedName

data class BaseWeather(
    @SerializedName("lat")
    var lat:Double,
    @SerializedName("lon")
    var lon :Double,
    @SerializedName("timezone")
    var timezone :String,
    @SerializedName("timezone_offset")
    var timezone_offset: Double,
    @SerializedName("current")
    var current: Current,
    @Ignore
    @SerializedName("hourly")
    var hourly: List<Hourly>,
    @Ignore
    @SerializedName("daily")
    var daily: List<Daily>,
    @SerializedName("alerts")
    var alert: List<AlertWeather>?
)