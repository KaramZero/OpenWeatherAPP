package com.example.openweather.view_model.weather_view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openweather.model.Location
import com.example.openweather.model.local_source.LocalSource
import com.example.openweather.model.repo.WeatherRepo
import com.example.openweather.model.weather_pojo.BaseWeather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherViewModel(var weatherRepo: WeatherRepo,var localSource: LocalSource) : ViewModel() {

    fun getWeather(lat : String, lon : String):LiveData<BaseWeather>{
        Log.i("TAG", "getWeather: WeatherViewModel ")

            return weatherRepo.getWeather(lat , lon)
    }
    fun insertLocation(location: Location){
        localSource.insertLocation(location)
    }
    fun deleteLocation(location: Location){
        localSource.deleteLocation(location)
    }
    fun getAllStoredLocations():LiveData<List<Location>>{
        return localSource.getAllStoredLocations()
    }

}