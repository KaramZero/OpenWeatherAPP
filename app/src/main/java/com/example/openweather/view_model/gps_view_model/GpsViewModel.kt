package com.example.openweather.view_model.gps_view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.openweather.model.repo.GpsRepo
import com.example.openweather.model.pojo.Location


class GpsViewModel(private var gpsRepo: GpsRepo) : ViewModel() {
    private var _location = gpsRepo.location
    val location:LiveData<Location> = _location
    fun getLocation(){
        Log.i("TAG", "getLocation: GpsViewModel ")
        _location = gpsRepo.location
        gpsRepo.getLocation()
    }
}