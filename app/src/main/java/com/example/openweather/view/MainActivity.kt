package com.example.openweather.view

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.openweather.databinding.ActivityMainBinding
import com.example.openweather.model.local_source.LocationsLocal
import com.example.openweather.model.pojo.Location
import com.example.openweather.model.remote_source.WeatherRemote
import com.example.openweather.model.repo.GpsRepo
import com.example.openweather.model.repo.WeatherRepo
import com.example.openweather.view.fav_locations.FavLocations
import com.example.openweather.view.main.SectionsPagerAdapter
import com.example.openweather.view_model.gps_view_model.GpsViewModel
import com.example.openweather.view_model.gps_view_model.factory.GpsViewModelFactory
import com.example.openweather.view_model.weather_view_model.WeatherViewModel
import com.example.openweather.view_model.weather_view_model.factory.WeatherViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fab: FloatingActionButton
    private lateinit var drawerMenuFab: FloatingActionButton
    private lateinit var drawerLayout: DrawerLayout


    companion object{
        lateinit var gpsViewModel : GpsViewModel
        lateinit var weatherViewModel : WeatherViewModel
        lateinit var lifecycleOwner: LifecycleOwner
        lateinit var lang:String
        lateinit var tempUnit:String
        lateinit var speedUnit:String
        private lateinit var settings: SharedPreferences
        private var gpsMode : Boolean = true
        private lateinit var lat :String
        private lateinit var lon :String
        lateinit var locationsData : LiveData<List<Location>>


        fun reload(settings: SharedPreferences){
            tempUnit = settings.getString("tempUnit","celsius").toString()
            speedUnit = settings.getString("speedUnit","meterSec").toString()
            lang = settings.getString("language","en").toString()
            gpsMode = settings.getBoolean("gpsMode",true)
            lat = settings.getString("lat","-35").toString()
            lon = settings.getString("lon","151").toString()

            if (gpsMode) {
                gpsViewModel.getLocation()
            }else{
                weatherViewModel.getWeather(lat,lon,lang)
            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermissions()

        initViewPager()

        initFloatingButtons()

        initDrawer()

        initViewModels()

        lifecycleOwner = this

        settings = getSharedPreferences("Settings", MODE_PRIVATE)

        weatherViewModel.getLastWeather()

        reload(settings)

        locationsData = weatherViewModel.storedLocations
        weatherViewModel.getAllStoredLocations()

        locationsData.observe(this){

        }
        gpsViewModel.location.observe(this) {
            weatherViewModel.getWeather(it.lat.toString(), it.lon.toString(),lang)
        }

    }

    private fun initViewModels(){
        val fusedLocationProviderClient:FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val gpsViewModelFactory = GpsViewModelFactory(GpsRepo.getGpsRepo(this,fusedLocationProviderClient))
        gpsViewModel =ViewModelProvider(this,gpsViewModelFactory)[GpsViewModel::class.java]


        val weatherViewModelFactory = WeatherViewModelFactory(WeatherRemote.getInstance()
            ?.let { WeatherRepo.getInstance(this, it) }!!,LocationsLocal.getInstance(this))

        weatherViewModel = ViewModelProvider(this,weatherViewModelFactory)[WeatherViewModel::class.java]

    }

    private fun initViewPager(){
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
    }

    private fun initFloatingButtons(){
         drawerMenuFab = binding.menuFab
         fab = binding.fab

        drawerMenuFab.setOnClickListener {
            drawerLayout.open()
        }
        fab.setOnClickListener{
           val intent = Intent(this, GoogleMapsActivity::class.java)
           startActivity(intent)
        }

    }

    private fun initDrawer(){
        drawerLayout = binding.myDrawer

        drawerLayout.addDrawerListener(object : DrawerListener {
            override fun onDrawerSlide(view: View, v: Float) {}
            override fun onDrawerOpened(view: View) {drawerMenuFab.hide(); fab.hide()}
            override fun onDrawerClosed(view: View) {drawerMenuFab.show(); fab.show()}

            override fun onDrawerStateChanged(i: Int) {}
        })
    }

    private fun checkPermissions(){
        if (ContextCompat.checkSelfPermission(this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@MainActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED)) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show()
                    finish()
                }
                return
            }
        }
    }

     fun onLocationsListClicked(item: MenuItem) {
        startActivity(Intent(this, FavLocations::class.java))
    }

    fun onSettingsClicked(item: MenuItem) {

        startActivity(Intent(this,Settings::class.java))
        drawerLayout.close()
    }

    fun onAboutUsClicked(item: MenuItem) {
        Toast.makeText(this,"Coming Soon :)",Toast.LENGTH_SHORT)
    }


    fun onWeatherMapClicked(item: MenuItem) {
        Toast.makeText(this,"Coming Soon :)",Toast.LENGTH_SHORT)
    }

}