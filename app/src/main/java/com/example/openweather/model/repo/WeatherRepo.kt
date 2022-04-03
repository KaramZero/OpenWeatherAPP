package com.example.openweather.model.repo

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.openweather.model.pojo.LastKnownWeather
import com.example.openweather.model.remote_source.RemoteSource
import com.example.openweather.model.pojo.weather_pojo.BaseWeather

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

    fun getWeather(lat : String , lon : String): LiveData<BaseWeather> {
        Log.i("TAG", "getWeather: WeatherRepo ")
        return remoteSource.enqueueCall(lat , lon)
    }



}