package com.example.openweather.view_model.weather_view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openweather.model.local_source.LocalSource
import com.example.openweather.model.pojo.LastKnownWeather
import com.example.openweather.model.pojo.Location
import com.example.openweather.model.pojo.weather_pojo.BaseWeather
import com.example.openweather.model.repo.WeatherRepo
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherViewModel(private var weatherRepo: WeatherRepo) : ViewModel() {

    private var _lastWeather = MutableLiveData<BaseWeather>()
    var lastWeather :LiveData<BaseWeather> = _lastWeather

    private var _storedLocations = MutableLiveData<List<Location>>()
    var storedLocations :LiveData<List<Location>> = _storedLocations

    fun getWeather(lat : String, lon : String,lang :String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _lastWeather.postValue(weatherRepo.getWeather(lat,lon,lang))
            }
        }
    }

    fun insertLocation(location: Location){
        weatherRepo.insertLocation(location)
    }
    fun deleteLocation(location: Location){
        weatherRepo.deleteLocation(location)
    }
    fun getAllStoredLocations(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _storedLocations.postValue(weatherRepo.getAllStoredLocations())
                Log.i("TAG", "getAllStoredLocations: size ${_storedLocations.value?.size}")
            }
        }

    }

    fun insertWeather(weather: BaseWeather){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                Log.i("TAG", "insertWeather: Done ")
                weatherRepo.insertWeather(weather)
            }

        }
    }

    fun getLastWeather(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val lastKnownWeather = weatherRepo.getLastWeather()
                if(lastKnownWeather != null){
                Log.i("TAG", "getLastWeather: Done $lastKnownWeather")
                val gson = Gson()
                val weather:BaseWeather = gson.fromJson(lastKnownWeather.data,BaseWeather::class.java)
                _lastWeather.postValue(weather)}
            }
        }
    }

}