package com.example.openweather.model.local_source

import androidx.lifecycle.LiveData
import com.example.openweather.model.pojo.LastKnownWeather
import com.example.openweather.model.pojo.Location

interface LocalSource {
    fun insertLocation(location: Location)
    fun deleteLocation(location: Location)
    fun getAllStoredLocations(): LiveData<List<Location>>
    fun getLastWeather(): LastKnownWeather
    fun insertWeather(lastKnownWeather: LastKnownWeather)
}