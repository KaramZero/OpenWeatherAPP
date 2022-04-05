package com.example.openweather.model.remote_source

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.openweather.model.pojo.weather_pojo.BaseWeather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {
    private var baseUrl = "https://api.openweathermap.org/data/2.5/"
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var baseWeather = MutableLiveData<BaseWeather>()

    fun makeRequest(lat : String , lon : String,lang :String): LiveData<BaseWeather> {
        val retrofitClientInterface: RetrofitClientInterface = retrofit.create(
            RetrofitClientInterface::class.java
        )

        Log.i("TAG", "makeRequest: ")

        retrofitClientInterface.getWeather(lat,lon,lang).enqueue(object : Callback<BaseWeather> {
            override fun onResponse(
                call: Call<BaseWeather>,
                response: Response<BaseWeather>
            ) {
                Log.i("TAG", "onResponse:  ")

                baseWeather.postValue(response.body())
                Log.i("TAG", "onResponse:  ")
            }

            override fun onFailure(call: Call<BaseWeather>, t: Throwable) {
                Log.i("TAG", "onFailure: ${t.message}")
            }
        })
        return baseWeather
    }

    companion object {
        private var retrofitClient: RetrofitClient? = null
        fun getRetrofitInstance(): RetrofitClient? {
            if (retrofitClient == null) {
                retrofitClient = RetrofitClient()
            }
            return retrofitClient
        }
    }


}