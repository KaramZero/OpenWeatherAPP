package com.example.openweather.view.seven_days

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.openweather.R
import com.example.openweather.model.pojo.weather_pojo.BaseWeather
import com.example.openweather.model.pojo.weather_pojo.Daily
import com.example.openweather.model.pojo.weather_pojo.Hourly
import com.example.openweather.view.MainActivity
import java.util.*
import kotlin.Int
import kotlin.collections.ArrayList

class SevenDaysRecyclerAdapter(
    private val context: Context
) : RecyclerView.Adapter<SevenDaysRecyclerAdapter.ViewHolder>() {

    private var dailyWeather: List<Daily> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.seven_days_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val iconUrl =
            "https://openweathermap.org/img/wn/${dailyWeather[position].weather[0].icon}@2x.png"
        Glide.with(context).load(iconUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
            )
            .into(holder.imageView)

        holder.description.text = dailyWeather[position].weather[0].description
        holder.dayNumber.text = (position+1).toString()
        when(MainActivity.tempUnit){
            "celsius" -> {
                holder.maxTemp.text = (dailyWeather[position].temp.max- 273.15).toInt().toString()
                holder.minTemp.text = (dailyWeather[position].temp.min- 273.15).toInt().toString()

            }
            "fahrenheit" -> {
                holder.maxTemp.text = ((dailyWeather[position].temp.max - 273.15)*9/5+32).toInt().toString()
                holder.minTemp.text = ((dailyWeather[position].temp.min - 273.15)*9/5+32).toInt().toString()

            }
            "kelvin" -> {
                holder.maxTemp.text = dailyWeather[position].temp.max.toInt().toString()
                holder.minTemp.text = dailyWeather[position].temp.min.toInt().toString()
            }
        }

    }

    override fun getItemCount(): Int {
        return dailyWeather.size-1
    }


    fun setDailyWeatherList(daily: List<Daily>) {
        this.dailyWeather = daily
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.Days_icon)
        var maxTemp: TextView = itemView.findViewById(R.id.Days_max_temp)
        var minTemp: TextView = itemView.findViewById(R.id.Days_min_temp)
        var description:TextView = itemView.findViewById(R.id.Days_description)
        var dayNumber:TextView = itemView.findViewById(R.id.Day_number)

    }

}
