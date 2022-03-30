package com.example.openweather.view.tomorrow

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.openweather.R
import com.example.openweather.model.weather_pojo.BaseWeather
import com.example.openweather.view.MainActivity
import com.example.openweather.view.today.TodayRecyclerAdapter


class TomorrowFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tomorrow, container, false)
    }

    private lateinit var myView: View
    private lateinit var weatherIcon : ImageView
    private lateinit var locationTextView : TextView
    private lateinit var tempTextView : TextView
    private lateinit var feelsLikeTextView : TextView
    private lateinit var descriptionTextView : TextView
    private lateinit var recyclerView : RecyclerView
    private lateinit var todayRecyclerAdapter: TodayRecyclerAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myView = view
        initViews()

        MainActivity.gpsViewModel.location.observe(viewLifecycleOwner){
            Log.i("TAG", "gpsViewModel.location : observer ")
            getWeather(it.lat.toString(),it.long.toString())
        }


    }

    private fun initViews(){
        weatherIcon = myView.findViewById(R.id.tomorrow_weather_icon)
        locationTextView = myView.findViewById(R.id.tomorrow_location)
        tempTextView = myView.findViewById(R.id.tomorrow_temp)
        feelsLikeTextView = myView.findViewById(R.id.tomorrow_feels_like)
        descriptionTextView = myView.findViewById(R.id.tomorrow_weather_description)
        recyclerView  = myView.findViewById(R.id.tomorrow_RecyclerView_weather_for24H)

        todayRecyclerAdapter = TodayRecyclerAdapter(myView.context)

        val linearLayoutManager = LinearLayoutManager(myView.context)
        linearLayoutManager.orientation = RecyclerView.HORIZONTAL

        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = todayRecyclerAdapter

    }

    private fun getWeather(lat : String, lon : String){
        MainActivity.weatherViewModel.getWeather(lat,lon).observe(viewLifecycleOwner){
            Log.i("TAG", "onCreate: Weather is here ")
            setWeather(it)
        }
    }

    private fun setWeather(baseWeather: BaseWeather){
        locationTextView.text = baseWeather.timezone
        tempTextView.text = (baseWeather.daily[1].temp.day - 273.15).toInt().toString()
        feelsLikeTextView.text = (baseWeather.daily[1].feels_like.day - 273.15).toInt().toString()
        descriptionTextView.text = baseWeather.daily[1].weather[0].description

        var iconUrl =
            "https://openweathermap.org/img/wn/${baseWeather.daily[1].weather[0].icon}@2x.png"
        Log.i("TAG", "setWeather: iconUrl   $iconUrl")
        Glide.with(myView.context).load(iconUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
            )
            .into(weatherIcon)


        todayRecyclerAdapter.setHourlyTempList(baseWeather.hourly,24)
        todayRecyclerAdapter.notifyDataSetChanged()
    }

}