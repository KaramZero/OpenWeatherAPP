package com.example.openweather.model.local_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.openweather.model.pojo.LastKnownWeather
import com.example.openweather.model.pojo.Location

@Dao
interface LocationDao {
    @Query("SELECT * From locations")
    fun getAllLocations(): List<Location>

    @Query("SELECT * From lastWeather LIMIT 1")
    fun getLastWeather(): LastKnownWeather

    @Insert
    fun insertWeather(lastKnownWeather: LastKnownWeather)


    @Query("DELETE FROM lastWeather")
    fun deleteWeather()

    @Insert
    fun insertLocation(location: Location?)

    @Delete
    fun deleteLocation(location: Location?)
}