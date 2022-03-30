package com.example.openweather.view

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.openweather.R
import com.example.openweather.databinding.ActivityGoogleMapsBinding
import com.example.openweather.model.Location
import com.example.openweather.model.local_source.LocationsLocal
import com.example.openweather.model.remote_source.WeatherRemote
import com.example.openweather.model.repo.WeatherRepo
import com.example.openweather.view_model.weather_view_model.WeatherViewModel
import com.example.openweather.view_model.weather_view_model.factory.WeatherViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*


class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityGoogleMapsBinding
    private lateinit var confirmButton: Button
    private lateinit var pickedLocationTextView: TextView
    private var location = Location()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGoogleMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        pickedLocationTextView = findViewById(R.id.pickedLocation)
        confirmButton = findViewById(R.id.pickLocationButton)
        confirmButton.setOnClickListener {
            MainActivity.weatherViewModel.insertLocation(location)
            finish()
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)

        var marker = mMap.addMarker(
            MarkerOptions().position(sydney).draggable(true).title("Marker in Sydney")
        )
        mMap.moveCamera(CameraUpdateFactory.zoomBy(15f))

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))


        mMap.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
            override fun onMapClick(p0: LatLng) {
                Log.i("TAG", "onMapClick: ")
                if (marker == null) return

                marker.position = p0

                val geocoder = Geocoder(this@GoogleMapsActivity, Locale.getDefault())
                val addresses: List<Address>?
                try {
                    addresses = geocoder.getFromLocation(p0.latitude, p0.longitude, 1)
                    if (!addresses.isNullOrEmpty()) {
                        pickedLocationTextView.text = addresses[0].locality
                        marker.title = addresses[0].locality
                        location.lat = p0.latitude
                        location.long = p0.longitude
                        location.city = addresses[0].locality
                        marker.title = addresses[0].locality
                        Log.i("TAG", "getLocation: addOnCompleteListener ${addresses[0].locality}")
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

        })

        mMap.setOnMarkerDragListener(object : OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {
            }

            override fun onMarkerDrag(marker: Marker) {

            }

            override fun onMarkerDragEnd(marker: Marker) {

                if (marker == null) return

                val geocoder = Geocoder(this@GoogleMapsActivity, Locale.getDefault())
                val addresses: List<Address>?
                try {
                    addresses = geocoder.getFromLocation(marker.position.latitude, marker.position.longitude, 1)
                    if (!addresses.isNullOrEmpty()) {
                        pickedLocationTextView.text = addresses[0].locality
                        marker.title = addresses[0].locality
                        location.lat = marker.position.latitude
                        location.long = marker.position.longitude
                        location.city = addresses[0].locality
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                Log.i("TAG", "onMarkerDragEnd: ${marker.position}")
            }
        })

    }


}