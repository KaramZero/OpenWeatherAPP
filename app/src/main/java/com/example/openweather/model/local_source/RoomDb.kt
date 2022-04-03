package com.example.openweather.model.local_source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.openweather.model.pojo.LastKnownWeather
import com.example.openweather.model.pojo.Location

@Database(entities = [Location::class,LastKnownWeather::class], version = 2)
abstract class RoomDb : RoomDatabase() {
    abstract fun locationDao(): LocationDao?

    companion object {
        private var roomDb: RoomDb? = null
        @Synchronized
        fun getInstance(context: Context?): RoomDb? {
            if (roomDb == null) {
                roomDb = Room.databaseBuilder(context!!, RoomDb::class.java, "Locations").build()
            }
            return roomDb
        }
    }
}