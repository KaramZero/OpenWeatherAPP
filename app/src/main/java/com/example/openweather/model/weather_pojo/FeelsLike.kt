package com.example.openweather.model.weather_pojo

import com.google.gson.annotations.SerializedName

data class FeelsLike(
    @SerializedName("day")
    var day :Float,
    @SerializedName("night")
    var night :Float,
    @SerializedName("eve")
    var eve :Float,
    @SerializedName("morn")
    var morn :Float

) {
}