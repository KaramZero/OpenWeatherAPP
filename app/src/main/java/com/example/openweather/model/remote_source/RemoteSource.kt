package com.example.openweather.model.remote_source

import androidx.lifecycle.LiveData
import com.example.openweather.model.pojo.weather_pojo.BaseWeather

interface RemoteSource {
    fun enqueueCall(lat: String, lon: String): LiveData<BaseWeather>
}