package com.example.openweather.view.fav_locations

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.openweather.R
import com.example.openweather.model.pojo.Location
import com.example.openweather.view.GoogleMapsActivity
import com.example.openweather.view.MainActivity

class FavLocations : AppCompatActivity() {

    lateinit var floatingActionButton: FloatingActionButton
    private lateinit var adapter : FavLocationsRecyclerAdapter

    companion object{
        private lateinit var settings: SharedPreferences
        fun disableGPS(newLocation: Location){
            settings.edit().putBoolean("gpsMode",false)
                .putString("lat",newLocation.lat.toString())
                .putString("lon",newLocation.lon.toString()).apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fav_locations)
        setSupportActionBar(findViewById(R.id.toolbar))

        settings = getSharedPreferences("Settings", MODE_PRIVATE)
        val recyclerView :RecyclerView = findViewById(R.id.RecyclerView_locations)

        adapter = FavLocationsRecyclerAdapter(this)
        adapter.setLocationsList()
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL

        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter


        floatingActionButton = findViewById(R.id.addNewLocationFab)

        floatingActionButton.setOnClickListener { view ->
            startActivity(Intent(this,GoogleMapsActivity::class.java))
        }

        val button :Button = findViewById(R.id.useGPSButton)
        button.setOnClickListener{
            MainActivity.gpsViewModel.getLocation()
            settings.edit().putBoolean("gpsMode",true).apply()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        MainActivity.weatherViewModel.getAllStoredLocations()
        MainActivity.locationsData.observe(this){
            adapter.setLocationsList()
            adapter.notifyDataSetChanged()
        }

    }

}