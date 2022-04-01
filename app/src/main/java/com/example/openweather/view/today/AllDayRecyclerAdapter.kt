package com.example.openweather.view.today

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
import com.example.openweather.model.weather_pojo.Hourly
import java.util.*
import kotlin.Int
import kotlin.collections.ArrayList

class AllDayRecyclerAdapter(
    private val context: Context
) : RecyclerView.Adapter<AllDayRecyclerAdapter.ViewHolder>() {

    private var hourlyTempList: List<Hourly> = ArrayList()
    private var interval : Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.list_item24h, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val index= position+interval
        val iconUrl =
            "https://openweathermap.org/img/wn/${hourlyTempList[index].weather[0].icon}@2x.png"
        Glide.with(context).load(iconUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
            )
            .into(holder.imageView)

        holder.temp.text = (hourlyTempList[index].temp- 273.15).toInt().toString()

        val calendar = Calendar.getInstance()
        calendar.timeInMillis =hourlyTempList[index].dt.toLong()*1000
        val hour = calendar.get(Calendar.HOUR)

        Log.i("TAG", "onBindViewHolder: ${calendar.time}  ,hours: $hour index: $index")
        holder.hour.text = hour.toString()



    }

    override fun getItemCount(): Int {
        return hourlyTempList.size/2
    }


    fun setHourlyTempList(hourlyTempList: List<Hourly>,interval:Int = 0) {
        this.interval = interval
        this.hourlyTempList = hourlyTempList
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.imageView24H)
        var temp: TextView = itemView.findViewById(R.id.temp_24h)
        var hour: TextView = itemView.findViewById(R.id.temp_hour24)

    }

}
