package com.example.openweather.view.alerts

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.text.format.DateFormat
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.openweather.R
import com.example.openweather.databinding.ActivityAlertsBinding
import com.example.openweather.view.MyWorker
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

class Alerts : AppCompatActivity() {

    private lateinit var binding: ActivityAlertsBinding
    private lateinit var alerts: SharedPreferences
    private lateinit var adapter : AlertsRecyclerAdapter
    private var days: Int = 0


    companion object{
        var hour: MutableLiveData<Int> = MutableLiveData()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAlertsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title

        alerts = getSharedPreferences("Alerts", MODE_MULTI_PROCESS)

        binding.fab.setOnClickListener {
            showDialog()
        }


        val recyclerView : RecyclerView = findViewById(R.id.RecyclerView_locations)

        adapter = AlertsRecyclerAdapter(alerts)
        adapter.setAlertsList(alerts.all as HashMap<String, Int>)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL

        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter

        val button : Button = findViewById(R.id.useGPSButton)
        button.text = getText(R.string.clearAllAlerts)
        button.setOnClickListener{
            alerts.edit().clear().apply()
            adapter.setAlertsList(alerts.all as HashMap<String, Int>)
        }

        hour.observe(this){
            if (days > 0)
            addAlert()
        }

    }

    private fun showDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.DaysNumber))

        val input = EditText(this)
        input.hint = getString(R.string.DaysNumber)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ ->

            if (input.text.toString().isNotEmpty()) {
                days = input.text.toString().toInt()
                TimePickerFragment().show(supportFragmentManager, "timePicker")
            }
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            // Use the current time as the default values for the picker
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            // Create a new instance of TimePickerDialog and return it
            return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
        }

        override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
            hour.postValue(hourOfDay*60 + minute)
        }
    }

    private fun addAlert(){
        val alertTime = hour.value
        val currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)*60 + Calendar.getInstance().get(Calendar.MINUTE)
        var delay = 0
        alerts.edit().putInt(alertTime.toString(),days).apply()
        adapter.setAlertsList(alerts.all as HashMap<String, Int>)

        if (alertTime != null) {
            when{
                alertTime > currentTime -> delay= alertTime - currentTime
                alertTime < currentTime -> delay = 24*60 -currentTime - alertTime
                alertTime == currentTime -> delay = 24*60
            }




        }
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val req : OneTimeWorkRequest = OneTimeWorkRequest
            .Builder(MyWorker::class.java)
            .addTag(alertTime.toString())
            .setConstraints(constraints)
            .setInitialDelay(delay.toLong(), TimeUnit.MINUTES)
            .build()

        Log.i("TAG", "addAlert: after $delay Minutes ")
        WorkManager.getInstance(this).enqueue(req)

    }

}