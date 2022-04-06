package com.example.openweather.view.seven_days

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.openweather.R
import com.example.openweather.view.MainActivity
import com.example.openweather.view.fav_locations.FavLocationsRecyclerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class SevenDaysFragment : Fragment() {

    private lateinit var adapter : SevenDaysRecyclerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seven_days, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView : RecyclerView = view.findViewById(R.id.RecyclerView_seven_days)

        adapter = SevenDaysRecyclerAdapter(requireContext())
        adapter.setDailyWeatherList(MainActivity.weatherViewModel.lastWeather.value!!.daily)

        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}