package com.example.openweather.model.weather_pojo

import com.google.gson.annotations.SerializedName

data class Current(
    @SerializedName("dt")
    var dt: Int,
    @SerializedName("sunrise")
    var sunrise: Int,
    @SerializedName("sunset")
    var sunset: Int,
    @SerializedName("temp")
    var temp: Double,
    @SerializedName("feels_like")
    var feels_like: Double,
    @SerializedName("pressure")
    var pressure: Int,
    @SerializedName("humidity")
    var humidity: Int,
    @SerializedName("dew_point")
    var dew_point: Double,
    @SerializedName("uvi")
    var uvi: Double,
    @SerializedName("clouds")
    var clouds: Int,
    @SerializedName("visibility")
    var visibility: Int,
    @SerializedName("wind_speed")
    var wind_speed: Double,
    @SerializedName("wind_deg")
    var wind_deg: Int,
    @SerializedName("weather")
    var weather: List<Weather>,
    @SerializedName("rain")
   var rain: Rain

)