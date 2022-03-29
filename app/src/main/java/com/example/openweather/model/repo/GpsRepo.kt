package com.example.openweather.model.repo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.example.openweather.model.Location
import com.google.android.gms.location.*
import java.io.IOException
import java.util.*

class GpsRepo private constructor(var context: Context, private var fusedLocationProviderClient: FusedLocationProviderClient) {
    companion object{
        private var gpsRepo : GpsRepo? = null
        fun getGpsRepo(_context: Context, _fusedLocationProviderClient: FusedLocationProviderClient): GpsRepo {
            if (gpsRepo == null)
                gpsRepo = GpsRepo(_context,_fusedLocationProviderClient)
            return gpsRepo as GpsRepo
        }
    }
     var location = MutableLiveData<Location>()

    fun getLocation(){

        Log.i("TAG", "getLocation: ---- ")
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.i("TAG", "getLocation: returned ")
            return
        }
        fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
            val location = task.result
            if (location == null) {
                requestNewLocation()
            } else {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses: List<Address>?
                try {
                    val lat = location.latitude
                    val lon = location.longitude
                    addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    this.location.postValue(Location(lat,lon, addresses[0].locality))
                    Log.i("TAG", "getLocation: addOnCompleteListener ${addresses[0].locality}")
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }


    }

    private fun requestNewLocation() {
        Log.i("TAG", "requestNewLocation: ")
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val locationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses: List<Address>?
                try {
                   val lat = locationResult.lastLocation.latitude
                    val lon = locationResult.lastLocation.longitude
                    addresses = geocoder.getFromLocation(
                        locationResult.lastLocation.latitude,
                        locationResult.lastLocation.longitude,
                        1
                    )
                    location.postValue(Location(lat,lon, addresses[0].locality))
                    Log.i("TAG", "onLocationResult: requestNewLocation ${addresses[0].locality} ")

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback,
            Looper.myLooper()!!
        )
    }
}