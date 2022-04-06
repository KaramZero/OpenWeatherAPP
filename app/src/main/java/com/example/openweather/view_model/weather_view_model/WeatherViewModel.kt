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
import com.example.openweather.view.MainActivity
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherViewModel(var weatherRepo: WeatherRepo,var localSource: LocalSource) : ViewModel() {

    private var _lastWeather = MutableLiveData<BaseWeather>()
    var lastWeather = _lastWeather

    private var _storedLocations = MutableLiveData<List<Location>>()
    var storedLocations :LiveData<List<Location>> = _storedLocations

    fun getWeather(lat : String, lon : String,lang :String){
        var baseWeather = MutableLiveData<BaseWeather>()
        viewModelScope.launch {
             baseWeather = weatherRepo.getWeather(lat,lon,lang) as MutableLiveData<BaseWeather>
        }
        baseWeather.observe(MainActivity.lifecycleOwner){
            Log.i("TAG", "getWeather: WeatherViewModel ")
            _lastWeather.postValue(it)

        }
    }
    fun insertLocation(location: Location){
        localSource.insertLocation(location)
    }
    fun deleteLocation(location: Location){
        localSource.deleteLocation(location)
    }
    fun getAllStoredLocations(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _storedLocations.postValue(localSource.getAllStoredLocations())
                Log.i("TAG", "getAllStoredLocations: size ${_storedLocations.value?.size}")
            }
        }

    }

    fun insertWeather(weather: BaseWeather){
        viewModelScope.launch {
            val gson = Gson()
            val weather:String = gson.toJson(weather)

            withContext(Dispatchers.IO){
                Log.i("TAG", "insertWeather: Done ")
                localSource.insertWeather(LastKnownWeather(data = weather))
            }

        }
    }

    fun getLastWeather(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val lastKnownWeather = localSource.getLastWeather()
                if(lastKnownWeather != null){
                Log.i("TAG", "getLastWeather: Done $lastKnownWeather")
                val gson = Gson()
                val weather:BaseWeather = gson.fromJson(lastKnownWeather.data,BaseWeather::class.java)
                _lastWeather.postValue(weather)}
            }
        }
    }

}