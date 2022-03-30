package com.example.openweather.model.local_source

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.example.openweather.model.Location

class LocationsLocal private constructor(context: Context) : LocalSource {
    private var locationDao: LocationDao
    private var storedLocations: LiveData<List<Location>>


    companion object {
        private var locationsLocal: LocationsLocal? = null
        private  var context: Context? = null

        fun getInstance(context: Context): LocationsLocal {
            this.context = context
            if (locationsLocal == null) {
                locationsLocal = LocationsLocal(context)
            }
            return locationsLocal as LocationsLocal
        }
    }


    init {
        val database: RoomDb = RoomDb.getInstance(context)!!
        locationDao = database.locationDao()!!
        storedLocations = locationDao.getAllLocations()
    }


    override fun insertLocation(location: Location) {
        Thread {
            locationDao.deleteLocation(location)
            locationDao.insertLocation(location)
            Log.i("TAG", "insertLocation: ${location.city} is inserted")
        }.start()
    }

    override fun deleteLocation(location: Location) {
        Thread { locationDao.deleteLocation(location) }.start()
    }

    override fun getAllStoredLocations(): LiveData<List<Location>> {
        return storedLocations
    }
}