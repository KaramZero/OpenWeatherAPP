package com.example.openweather.model.pojo.weather_pojo

import com.google.gson.annotations.SerializedName

data class Daily(
    @SerializedName("dt")
    var dt :Int,
    @SerializedName("sunrise")
    var sunrise :Int,
    @SerializedName("sunset")
    var sunset :Int,
    @SerializedName("moonrise")
    var moonrise :Int,
    @SerializedName("moonset")
    var moonset :Int,
    @SerializedName("moon_phase")
    var moon_phase :Float,
    @SerializedName("temp")
    var temp : Temp,
    @SerializedName("feels_like")
    var feels_like : FeelsLike,
    @SerializedName("pressure")
    var pressure :Int,
    @SerializedName("humidity")
    var humidity :Int,
    @SerializedName("dew_point")
    var dew_point :Float,
    @SerializedName("wind_speed")
    var wind_speed :Float,
    @SerializedName("wind_deg")
    var wind_deg :Int,
    @SerializedName("weather")
    var weather :List<Weather>,
    @SerializedName("clouds")
    var clouds :Int,
    @SerializedName("pop")
    var pop :Float,
    @SerializedName("rain")
    var rain :Float,
    @SerializedName("uvi")
    var uvi :Float,


    ) {
}