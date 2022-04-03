package com.example.openweather.view.today

import android.app.AlertDialog
import android.app.Dialog
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
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.openweather.R
import com.example.openweather.model.pojo.Location
import com.example.openweather.model.pojo.weather_pojo.BaseWeather
import com.example.openweather.view.MainActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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




        MainActivity.gpsViewModel.location.observe(viewLifecycleOwner) {
            Log.i("TAG", "gpsViewModel.location : observer ")
            getWeather(it.lat.toString(), it.long.toString())
        }


    }

    private fun initViews() {
        weatherIcon = myView.findViewById(R.id.weather_icon)
        locationTextView = myView.findViewById(R.id.location)
        dateTextView = myView.findViewById(R.id.Date_and_Time)
        tempTextView = myView.findViewById(R.id.temp)
        feelsLikeTextView = myView.findViewById(R.id.feels_like)
        descriptionTextView = myView.findViewById(R.id.weather_description)
        recyclerView = myView.findViewById(R.id.RecyclerView_weather_for24H)
        constraintLayout = myView.findViewById(R.id.today_frameLayout)

        allDayRecyclerAdapter = AllDayRecyclerAdapter(myView.context)

        val linearLayoutManager = LinearLayoutManager(myView.context)
        linearLayoutManager.orientation = RecyclerView.HORIZONTAL

        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = allDayRecyclerAdapter


    }

    private fun getWeather(lat: String, lon: String) {
        MainActivity.weatherViewModel.getWeather(lat, lon)
    }

    private fun setWeather(baseWeather: BaseWeather) {

        val geocoder = Geocoder(this.context, Locale.getDefault())
        val addresses: List<Address>? = geocoder.getFromLocation(baseWeather.lat, baseWeather.lon, 1)
        locationTextView.text = addresses!![0].locality
        tempTextView.text = (baseWeather.current.temp - 273.15).toInt().toString()
        feelsLikeTextView.text = (baseWeather.current.feels_like - 273.15).toInt().toString()
        descriptionTextView.text = baseWeather.current.weather[0].description

        val calendar = Calendar.getInstance()

        dateTextView.text =
            SimpleDateFormat("MMMM.dd  hh:mm aaa", Locale.getDefault()).format(calendar.time)


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


        constraintLayout.background = getDrawable(baseWeather.current.weather[0].main)

        allDayRecyclerAdapter.setHourlyTempList(baseWeather.hourly)
        allDayRecyclerAdapter.notifyDataSetChanged()
    }


    private fun getDrawable(description: String): Drawable {
        val calendar = Calendar.getInstance()
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


    class StartGameDialogFragment(var locationsData : LiveData<List<Location>>) : DialogFragment() {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                var list = ArrayList<CharSequence>()
                var locationsList = locationsData.value
                if (locationsList != null) {
                    for (location in locationsList){
                        list.add(location.city)
                        Log.i("TAG", "onCreateDialog: city to list  ${location.city} ")
                    }
                }
                val myArray3 = list.toTypedArray()
                    builder.setTitle("Pick Location")
                    .setItems(
                        myArray3
                    ) { dialog, which ->
                        // The 'which' argument contains the index position
                        // of the selected item
                        var location = locationsList?.get(which)
                        if (location != null) {
                            Log.i("TAG", "onCreateDialog: calling location for ${location.city}")
                            MainActivity.weatherViewModel.getWeather(location.lat.toString(), location.long.toString())
                        }
                    }
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }


}