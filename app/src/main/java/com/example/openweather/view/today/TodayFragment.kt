package com.example.openweather.view.today


import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.openweather.R
import com.example.openweather.model.pojo.weather_pojo.BaseWeather
import com.example.openweather.view.MainActivity
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class TodayFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today, container, false)
    }


    private lateinit var myView: View
    private lateinit var weatherIcon: ImageView
    private lateinit var locationTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var tempTextView: TextView
    private lateinit var feelsLikeTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var unitTextView: TextView
    private lateinit var humidityTextView: TextView
    private lateinit var cloudTextView: TextView
    private lateinit var pressureTextView: TextView
    private lateinit var windTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var allDayRecyclerAdapter: AllDayRecyclerAdapter
    private lateinit var constraintLayout: ConstraintLayout



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myView = view
        initViews()

        MainActivity.weatherViewModel.lastWeather.observe(viewLifecycleOwner) {
            Log.i("TAG", "onCreate: Weather is here ")
            MainActivity.weatherViewModel.insertWeather(it)
            setWeather(it)
        }


    }

    private fun initViews() {
        weatherIcon = myView.findViewById(R.id.weather_icon)
        locationTextView = myView.findViewById(R.id.location)
        dateTextView = myView.findViewById(R.id.Date_and_Time)
        tempTextView = myView.findViewById(R.id.temp)
        feelsLikeTextView = myView.findViewById(R.id.feels_like)
        descriptionTextView = myView.findViewById(R.id.weather_description)
        unitTextView = myView.findViewById(R.id.unit)
        humidityTextView = myView.findViewById(R.id.humidity)
        cloudTextView = myView.findViewById(R.id.cloud)
        pressureTextView = myView.findViewById(R.id.pressure)
        windTextView = myView.findViewById(R.id.wind)
        constraintLayout = myView.findViewById(R.id.today_frameLayout)
        recyclerView = myView.findViewById(R.id.RecyclerView_weather_for24H)

        allDayRecyclerAdapter = AllDayRecyclerAdapter(myView.context)

        val linearLayoutManager = LinearLayoutManager(myView.context)
        linearLayoutManager.orientation = RecyclerView.HORIZONTAL

        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = allDayRecyclerAdapter


    }


    private fun setWeather(baseWeather: BaseWeather) {

        val geocoder = Geocoder(this.context, Locale(MainActivity.lang))
        val addresses: List<Address>? = geocoder.getFromLocation(baseWeather.lat, baseWeather.lon, 1)
        locationTextView.text = addresses!![0].locality

        val numberFormat = NumberFormat.getInstance(Locale(MainActivity.lang))
        descriptionTextView.text = baseWeather.current.weather[0].description
        humidityTextView.text = numberFormat.format(baseWeather.current.humidity).toString()
        pressureTextView.text = numberFormat.format(baseWeather.current.pressure).toString()
        cloudTextView.text = numberFormat.format(baseWeather.current.clouds).toString()

        when(MainActivity.speedUnit){
            "meterSec" -> windTextView.text = numberFormat.format(baseWeather.current.wind_speed.toInt()).toString()
            "milesHour" -> windTextView.text = numberFormat.format((baseWeather.current.wind_speed*2.237).toInt()).toString()
        }

        when(MainActivity.tempUnit){
            "celsius" -> {
                tempTextView.text = numberFormat.format((baseWeather.current.temp - 273.15).toInt()).toString()
                feelsLikeTextView.text = numberFormat.format((baseWeather.current.feels_like - 273.15).toInt()).toString()
                unitTextView.text = "c"
            }
            "fahrenheit" -> {
                tempTextView.text = numberFormat.format(((baseWeather.current.temp - 273.15)*9/5+32).toInt()).toString()
                feelsLikeTextView.text = numberFormat.format(((baseWeather.current.feels_like- 273.15)*9/5+32).toInt()).toString()
                unitTextView.text = "f"
            }
            "kelvin" -> {
                tempTextView.text = numberFormat.format(baseWeather.current.temp.toInt()).toString()
                feelsLikeTextView.text = numberFormat.format(baseWeather.current.feels_like.toInt()).toString()
                unitTextView.text = "k"
            }
        }


        val calendar = Calendar.getInstance(Locale(MainActivity.lang))

        dateTextView.text =
            SimpleDateFormat("MMMM.dd  hh:mm aaa", Locale(MainActivity.lang)).format(calendar.time)


        val iconUrl =
            "https://openweathermap.org/img/wn/${baseWeather.current.weather[0].icon}@2x.png"
       // Log.i("TAG", "setWeather: iconUrl   $iconUrl")
        Glide.with(myView.context).load(iconUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
            )
            .into(weatherIcon)


        constraintLayout.background = getDrawable(baseWeather)

        allDayRecyclerAdapter.setHourlyTempList(baseWeather.hourly)
        allDayRecyclerAdapter.notifyDataSetChanged()
    }


    private fun getDrawable(baseWeather: BaseWeather): Drawable {
        val description: String =baseWeather.current.weather[0].main

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = baseWeather.current.dt.toLong()*1000
        var drawable = ContextCompat.getDrawable(myView.context, R.drawable.day_sunny_clear)

        if (calendar.get(Calendar.HOUR_OF_DAY) in 6..19) {
            when (description) {
                "Clear" -> drawable =
                    ContextCompat.getDrawable(myView.context, R.drawable.day_clear)
                "Clouds" -> drawable =
                    ContextCompat.getDrawable(myView.context, R.drawable.day_cloudy)
                "Thunderstorm" -> drawable =
                    ContextCompat.getDrawable(myView.context, R.drawable.day_sping)
                "Rain" -> drawable = ContextCompat.getDrawable(myView.context, R.drawable.day_rain)
                "Snow" -> drawable = ContextCompat.getDrawable(myView.context, R.drawable.snow)
            }
        } else {
            when (description) {
                "Clear" -> drawable =
                    ContextCompat.getDrawable(myView.context, R.drawable.night_clear)
                "Clouds" -> drawable =
                    ContextCompat.getDrawable(myView.context, R.drawable.night_cloudy)
                "Thunderstorm" -> drawable =
                    ContextCompat.getDrawable(myView.context, R.drawable.thunder)
                "Rain" -> drawable =
                    ContextCompat.getDrawable(myView.context, R.drawable.night_rain)
                "Snow" -> drawable = ContextCompat.getDrawable(myView.context, R.drawable.snow)
            }
        }
        return drawable!!
    }



}