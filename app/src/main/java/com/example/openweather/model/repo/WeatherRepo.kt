package com.example.openweather.model.repo

import android.content.Context
import android.util.Log
import com.example.openweather.model.pojo.weather_pojo.BaseWeather
import com.example.openweather.model.remote_source.RemoteSource

class WeatherRepo private constructor(
    var context: Context,
    private var remoteSource: RemoteSource
) {

    companion object {
        private var weatherRepo: WeatherRepo? = null

        fun getInstance(
            _context: Context,
            _remoteSource: RemoteSource
        ): WeatherRepo? {
            if (weatherRepo == null)
                weatherRepo = WeatherRepo(_context, _remoteSource)
            return weatherRepo
        }
    }

    suspend fun getWeather(lat : String , lon : String,lang:String): BaseWeather{
        Log.i("TAG", "getWeather: WeatherRepo ")
        return remoteSource.getWeather(lat , lon,lang)
    }



}