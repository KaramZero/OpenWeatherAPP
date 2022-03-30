package com.example.openweather.view.today

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.openweather.R
import com.example.openweather.model.weather_pojo.BaseWeather
import com.example.openweather.view.MainActivity

class TodayFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today, container, false)
    }


    private lateinit var myView: View
    private lateinit var weatherIcon : ImageView
    private lateinit var locationTextView :TextView
    private lateinit var dateTextView :TextView
    private lateinit var tempTextView :TextView
    private lateinit var feelsLikeTextView :TextView
    private lateinit var descriptionTextView :TextView
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
        weatherIcon = myView.findViewById(R.id.weather_icon)
        locationTextView = myView.findViewById(R.id.location)
        dateTextView = myView.findViewById(R.id.Date_and_Time)
        tempTextView = myView.findViewById(R.id.temp)
        feelsLikeTextView = myView.findViewById(R.id.feels_like)
        descriptionTextView = myView.findViewById(R.id.weather_description)
        recyclerView  = myView.findViewById(R.id.RecyclerView_weather_for24H)

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
        tempTextView.text = (baseWeather.current.temp - 273.15).toInt().toString()
        feelsLikeTextView.text = (baseWeather.current.feels_like - 273.15).toInt().toString()
        descriptionTextView.text = baseWeather.current.weather[0].description

        val iconUrl =
            "https://openweathermap.org/img/wn/${baseWeather.current.weather[0].icon}@2x.png"
        Log.i("TAG", "setWeather: iconUrl   $iconUrl")
        Glide.with(myView.context).load(iconUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
            )
            .into(weatherIcon)


        todayRecyclerAdapter.setHourlyTempList(baseWeather.hourly)
        todayRecyclerAdapter.notifyDataSetChanged()
    }

    class StartGameDialogFragment : DialogFragment() {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                val myArray3 = arrayOf("Abu","Praveen","Sathya","Yogesh","Ram")
                builder.setTitle("Pick Location")
                    .setItems(myArray3
                    ) { dialog, which ->
                        // The 'which' argument contains the index position
                        // of the selected item
                    }
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }

}