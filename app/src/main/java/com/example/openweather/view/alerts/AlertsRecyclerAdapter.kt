package com.example.openweather.view.alerts

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkManager
import com.example.openweather.R
import kotlin.Int
import kotlin.collections.ArrayList

class AlertsRecyclerAdapter(var alerts: SharedPreferences) : RecyclerView.Adapter<AlertsRecyclerAdapter.ViewHolder>() {

    private var map:HashMap<String,Int> = HashMap()
    private var keys: ArrayList<String> = ArrayList()
    private var values: ArrayList<Int> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.location_list_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val h:Int = keys[position].toInt()/60
        val m:Int = keys[position].toInt()%60

        holder.alert.text = h.toString() + ":"+ m+"     " + values[position]
    }

    override fun getItemCount(): Int {
        return keys.size
    }


    fun setAlertsList(map:HashMap<String,Int>) {
        this.map = map
        keys = ArrayList(map.keys)
        values = ArrayList(map.values)
        notifyDataSetChanged()
    }


   inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var alert: TextView = itemView.findViewById(R.id.locationName)
        private var button : ImageView = itemView.findViewById(R.id.removeLocation)

        init {

            button.setOnClickListener {
                WorkManager.getInstance(itemView.context).cancelAllWorkByTag(keys[adapterPosition])
                map.remove(keys[adapterPosition])
                alerts.edit().remove(keys[adapterPosition]).apply()
                setAlertsList(map)
            }
        }

   }

}
