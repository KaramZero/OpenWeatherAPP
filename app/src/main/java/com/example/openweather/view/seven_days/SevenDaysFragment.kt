package com.example.openweather.view.seven_days

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.openweather.R
import com.example.openweather.model.pojo.weather_pojo.Daily
import com.example.openweather.view.MainActivity
import java.text.NumberFormat
import java.util.*


class SevenDaysFragment : Fragment() {

    private lateinit var adapter: SevenDaysRecyclerAdapter

    private lateinit var myView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seven_days, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myView = view
        val recyclerView: RecyclerView = view.findViewById(R.id.RecyclerView_seven_days)

        adapter = SevenDaysRecyclerAdapter(requireContext(), this)
        adapter.setDailyWeatherList(MainActivity.weatherViewModel.lastWeather.value!!.daily)

        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    fun showDialog(daily: Daily) {

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.seven_days_hourly_list)
        val numberFormat = NumberFormat.getInstance(Locale(MainActivity.lang))

        val humidityTextView: TextView = dialog.findViewById(R.id.seven_humidity)
        val cloudTextView: TextView = dialog.findViewById(R.id.seven_cloud)
        val pressureTextView: TextView = dialog.findViewById(R.id.seven_pressure)
        val windTextView: TextView = dialog.findViewById(R.id.seven_wind)
        humidityTextView.text = numberFormat.format(daily.humidity).toString()
        cloudTextView.text = numberFormat.format(daily.clouds).toString()
        pressureTextView.text = numberFormat.format(daily.pressure).toString()
        windTextView.text = numberFormat.format(daily.wind_speed.toInt()).toString()

        dialog.show()
        val window: Window? = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}