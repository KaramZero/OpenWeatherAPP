package com.example.openweather.model.remote_source

import com.example.openweather.model.weather_pojo.BaseWeather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitClientInterface {
    @GET("onecall?appid=6f56af69aa243b830ba54b2ac9f5195d")
    fun getWeather(@Query("lat") lat:String, @Query("lon") lon:String): Call<BaseWeather>
}

//cce64fba5705becc7fbe52b46e9df003  3bdo
//