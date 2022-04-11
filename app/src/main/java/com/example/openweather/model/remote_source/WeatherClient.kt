package com.example.openweather.model.remote_source

import com.example.openweather.model.pojo.weather_pojo.BaseWeather


class MovieClient private constructor() : RemoteSource {

    override suspend fun getWeather(lat: String, lon: String, lang: String): BaseWeather {
        val movieService = RetrofitHelper.getInstance().create(WeatherService::class.java)
        return movieService.getWeather(lat, lon, lang)
    }

    companion object{
        private var instance: MovieClient? = null
        fun getInstance(): MovieClient {
            return  instance ?: MovieClient()
        }
    }

}