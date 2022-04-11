package com.example.openweather.view_model.weather_view_model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.openweather.model.local_source.LocalSource
import com.example.openweather.model.repo.WeatherRepo
import com.example.openweather.view_model.weather_view_model.WeatherViewModel

class WeatherViewModelFactory (private var weatherRepo: WeatherRepo, private var localSource: LocalSource): ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            WeatherViewModel(weatherRepo,localSource) as T
        } else {
            throw IllegalArgumentException("Error")
        }
    }
}