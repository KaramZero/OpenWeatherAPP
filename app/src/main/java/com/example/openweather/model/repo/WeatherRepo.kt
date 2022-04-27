package com.example.openweather.model.repo

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.openweather.model.local_source.LocalSource
import com.example.openweather.model.pojo.LastKnownWeather
import com.example.openweather.model.pojo.Location
import com.example.openweather.model.pojo.weather_pojo.BaseWeather
import com.example.openweather.model.remote_source.RemoteSource
import com.google.gson.Gson
class WeatherRepo private constructor(
    var context: Context,
    private var remoteSource: RemoteSource,
    private var localSource: LocalSource
) {

    companion object {
        private var weatherRepo: WeatherRepo? = null

        fun getInstance(
            _context: Context,
            _remoteSource: RemoteSource,
            _localSource: LocalSource
        ): WeatherRepo? {
            if (weatherRepo == null)
                weatherRepo = WeatherRepo(_context, _remoteSource, _localSource)
            return weatherRepo
        }
    }

    suspend fun getWeather(lat: String, lon: String, lang: String): BaseWeather {
        Log.i("TAG", "getWeather: WeatherRepo ")
        return if (isOnline(context)) {
            remoteSource.getWeather(lat, lon, lang)
        } else {
            val lastKnownWeather = localSource.getLastWeather()

            Log.i("TAG", "getLastWeather: Done $lastKnownWeather")
            val gson = Gson()
            gson.fromJson(lastKnownWeather.data, BaseWeather::class.java)
        }
    }


    fun insertLocation(location: Location) {
        localSource.insertLocation(location)
    }

    fun deleteLocation(location: Location) {
        localSource.deleteLocation(location)
    }

    fun getAllStoredLocations(): List<Location> {
        return localSource.getAllStoredLocations()
    }

    fun insertWeather(weather: BaseWeather) {
        val gson = Gson()
        val weatherSTR: String = gson.toJson(weather)
        Log.i("TAG", "insertWeather: Done ")
        localSource.insertWeather(LastKnownWeather(data = weatherSTR))
    }

    fun getLastWeather(): LastKnownWeather {
        return localSource.getLastWeather()
    }


    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        Log.i("TAG", "NetworkCapabilities.TRANSPORT_CELLULAR")
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        Log.i("TAG", "NetworkCapabilities.TRANSPORT_WIFI")
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        Log.i("TAG", "NetworkCapabilities.TRANSPORT_ETHERNET")
                        return true
                    }
                }
            }
        }
        Log.i("TAG", "No NetworkCapabilities")
        return false
    }

}