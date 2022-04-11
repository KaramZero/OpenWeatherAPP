package com.example.openweather.model.remote_source

import com.example.openweather.model.pojo.weather_pojo.BaseWeather

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("onecall?appid=6f56af69aa243b830ba54b2ac9f5195d")
    suspend fun getWeather(@Query("lat") lat:String, @Query("lon") lon:String, @Query("lang") lang:String) : BaseWeather
}