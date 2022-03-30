package com.example.openweather.model.local_source

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.openweather.model.Location

@Dao
interface LocationDao {
    @Query("SELECT * From locations")
    fun getAllLocations(): LiveData<List<Location>>

    @Insert
    fun insertLocation(location: Location?)

    @Delete
    fun deleteLocation(location: Location?)
}