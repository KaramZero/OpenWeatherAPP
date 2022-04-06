package com.example.openweather.view.fav_locations

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.openweather.R
import com.example.openweather.model.pojo.Location
import com.example.openweather.model.pojo.weather_pojo.Hourly
import com.example.openweather.view.MainActivity
import kotlin.Int
import kotlin.collections.ArrayList

class FavLocationsRecyclerAdapter(var appCompatActivity: AppCompatActivity) : RecyclerView.Adapter<FavLocationsRecyclerAdapter.ViewHolder>() {

    private var locationsList: ArrayList<Location> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.location_list_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.locationName.text = locationsList[position].city
    }

    override fun getItemCount(): Int {
        return locationsList.size
    }


    fun setLocationsList() {
        locationsList = MainActivity.locationsData.value as ArrayList<Location>
    }


   inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var locationName: TextView = itemView.findViewById(R.id.locationName)
        var button : Button = itemView.findViewById(R.id.removeLocation)

        init {
            itemView.setOnClickListener{
                MainActivity.weatherViewModel.getWeather(locationsList[adapterPosition].lat.toString(), locationsList[adapterPosition].lon.toString(),
                    MainActivity.lang
                )
                FavLocations.disableGPS(locationsList[adapterPosition])
                appCompatActivity.finish()
            }
            button.setOnClickListener{
                MainActivity.weatherViewModel.deleteLocation(locationsList[adapterPosition])
                Log.i("TAG", " list size :${locationsList.size} ")
                locationsList.removeAt(adapterPosition)
                Log.i("TAG", " list size :${locationsList.size} ")

                notifyDataSetChanged()
                MainActivity.weatherViewModel.getAllStoredLocations()
            }
        }

    }

}
