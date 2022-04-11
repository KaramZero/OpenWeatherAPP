package com.example.openweather.view

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.openweather.R
import com.example.openweather.databinding.ActivityGoogleMapsBinding
import com.example.openweather.model.pojo.Location
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

        supportActionBar?.hide()

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

        val marker = mMap.addMarker(
            MarkerOptions().position(sydney).draggable(true).title("Marker in Sydney")
        )

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(5f))



        mMap.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
            override fun onMapClick(p0: LatLng) {
                Log.i("TAG", "onMapClick: ")
                if (marker == null) return

                marker.position = p0

                val geocoder = Geocoder(this@GoogleMapsActivity, Locale.getDefault())
                val addresses: List<Address>?
                try {
                    addresses = geocoder.getFromLocation(p0.latitude, p0.longitude, 1)
                    if (!addresses.isNullOrEmpty()&& addresses[0].locality != null) {
                        pickedLocationTextView.text = addresses[0].locality
                        marker.title = addresses[0].locality
                        location.lat = p0.latitude
                        location.lon = p0.longitude
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
                        location.lon = marker.position.longitude
                        location.city = addresses[0].locality
                    }
                } catch (e: IOException) {
                    Log.i("TAG", "onMarkerDragEnd: geocoder IOException ")
                    finish()
                }
                Log.i("TAG", "onMarkerDragEnd: ${marker.position}")
            }
        })

    }


}