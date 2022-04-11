package com.example.openweather.model.remote_source

import com.example.openweather.model.pojo.weather_pojo.BaseWeather

interface RemoteSource {
    suspend fun getWeather(lat: String, lon: String, lang:String): BaseWeather
}