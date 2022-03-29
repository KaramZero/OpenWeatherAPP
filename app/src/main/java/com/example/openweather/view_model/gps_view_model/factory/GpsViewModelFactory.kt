package com.example.openweather.view_model.gps_view_model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.openweather.model.repo.GpsRepo
import com.example.openweather.view_model.gps_view_model.GpsViewModel

class GpsViewModelFactory(var gpsRepo: GpsRepo): ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(GpsViewModel::class.java)) {
            GpsViewModel(gpsRepo) as T
        } else {
            throw IllegalArgumentException("Error")
        }
    }
}