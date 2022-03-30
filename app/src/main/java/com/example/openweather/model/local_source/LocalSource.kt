package com.example.openweather.model.local_source

import androidx.lifecycle.LiveData
import com.example.openweather.model.Location

interface LocalSource {
    fun insertLocation(location: Location)
    fun deleteLocation(location: Location)
    fun getAllStoredLocations(): LiveData<List<Location>>
}