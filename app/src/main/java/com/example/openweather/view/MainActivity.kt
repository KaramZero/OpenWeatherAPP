package com.example.openweather.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.openweather.databinding.ActivityMainBinding
import com.example.openweather.model.repo.GpsRepo
import com.example.openweather.view.main.SectionsPagerAdapter
import com.example.openweather.view_model.gps_view_model.GpsViewModel
import com.example.openweather.view_model.gps_view_model.factory.GpsViewModelFactory
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
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermissions()

        initViewPager()

        initFloatingButtons()

        initDrawer()
        var fusedLocationProviderClient:FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        var gpsViewModelFactory = GpsViewModelFactory(GpsRepo.getGpsRepo(this,fusedLocationProviderClient))

        gpsViewModel =ViewModelProvider(this,gpsViewModelFactory)[GpsViewModel::class.java]
        gpsViewModel.getLocation()




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

    fun clickOnMe(item: MenuItem) {
        Toast.makeText(this,"Hi",Toast.LENGTH_SHORT).show()
    }


}