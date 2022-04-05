package com.example.openweather.model.remote_source

import androidx.lifecycle.LiveData
import com.example.openweather.model.pojo.weather_pojo.BaseWeather

class WeatherRemote private constructor() : RemoteSource {
    override fun enqueueCall(lat : String , lon : String,lang:String): LiveData<BaseWeather> {
        val retrofitClient: RetrofitClient =
            RetrofitClient.getRetrofitInstance()!!
        return retrofitClient.makeRequest(lat,lon,lang)
    }

    companion object {
        private var weatherRemote: WeatherRemote? = null
        fun getInstance(): WeatherRemote? {
            if (weatherRemote == null) {
                weatherRemote = WeatherRemote()
            }
            return weatherRemote
        }
    }
}