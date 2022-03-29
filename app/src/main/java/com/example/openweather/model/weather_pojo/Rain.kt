package com.example.openweather.model.weather_pojo

import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h")
    var hour :Double
  ) {
}