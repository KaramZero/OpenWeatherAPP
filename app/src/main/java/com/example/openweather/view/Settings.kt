package com.example.openweather.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.example.openweather.R

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    fun onTempUnitClicked(view: View) {}

    fun onSpeedUnitClicked(view: View){

    }

    fun onLangClicked(view: View){

    }

}