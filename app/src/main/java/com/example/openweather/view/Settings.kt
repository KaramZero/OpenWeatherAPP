package com.example.openweather.view

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.example.openweather.R

class Settings : AppCompatActivity() {

    private lateinit var kelvinRadioButton : RadioButton
    private lateinit var celsiusRadioButton : RadioButton
    private lateinit var fahrenheitRadioButton : RadioButton

    private lateinit var meterSecRadioButton : RadioButton
    private lateinit var milesHourRadioButton : RadioButton

    private lateinit var arabicRadioButton : RadioButton
    private lateinit var englishRadioButton : RadioButton

    private lateinit var settings: SharedPreferences

    private lateinit var tempUnit : String
    private lateinit var speedUnit : String
    private lateinit var language : String

    private val key = "Settings"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initViews()

        settings = getSharedPreferences(key, MODE_PRIVATE)

        tempUnit = settings.getString("tempUnit","celsius").toString()
        speedUnit = settings.getString("speedUnit","meterSec").toString()
        language = settings.getString("language","en").toString()

        setUnitsOnViews()

    }

    override fun onPause() {
        super.onPause()

        when {
            celsiusRadioButton.isChecked -> settings.edit().putString("tempUnit", "celsius").apply()
            fahrenheitRadioButton.isChecked -> settings.edit().putString("tempUnit", "fahrenheit").apply()
            kelvinRadioButton.isChecked -> settings.edit().putString("tempUnit", "kelvin").apply()
        }
        when{
            meterSecRadioButton.isChecked -> settings.edit().putString("speedUnit", "meterSec").apply()
            milesHourRadioButton.isChecked -> settings.edit().putString("speedUnit", "milesHour").apply()
        }
        when{
            arabicRadioButton.isChecked ->  settings.edit().putString("language", "ar").apply()
            englishRadioButton.isChecked -> settings.edit().putString("language", "en").apply()
        }

        MainActivity.reload(settings)
    }

    private fun setUnitsOnViews(){
        when(tempUnit){
            "celsius" ->celsiusRadioButton.isChecked = true
            "fahrenheit" -> fahrenheitRadioButton.isChecked = true
            "kelvin" -> kelvinRadioButton.isChecked = true
        }
        when(speedUnit){
            "meterSec" -> meterSecRadioButton.isChecked = true
            "milesHour" -> milesHourRadioButton.isChecked = true
        }
        when(language){
            "ar" -> arabicRadioButton.isChecked = true
            "en" -> englishRadioButton.isChecked =true
        }
    }
    private fun initViews(){
        kelvinRadioButton = findViewById(R.id.kelvin)
        celsiusRadioButton = findViewById(R.id.Celsius)
        fahrenheitRadioButton = findViewById(R.id.Fahrenheit)

        meterSecRadioButton = findViewById(R.id.meter_sec)
        milesHourRadioButton = findViewById(R.id.miles_hour)

        arabicRadioButton = findViewById(R.id.Arabic)
        englishRadioButton = findViewById(R.id.English)
    }

    fun onTempUnitClicked(view: View) {

    }

    fun onSpeedUnitClicked(view: View){

    }

    fun onLangClicked(view: View){

    }

}