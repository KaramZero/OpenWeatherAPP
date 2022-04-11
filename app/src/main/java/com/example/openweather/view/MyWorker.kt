package com.example.openweather.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import com.example.openweather.R
import com.example.openweather.model.remote_source.MovieClient
import com.example.openweather.model.remote_source.RemoteSource
import java.util.*
import java.util.concurrent.TimeUnit


class MyWorker(
    val
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private lateinit var settings: SharedPreferences
    private lateinit var alerts: SharedPreferences
    private lateinit var remoteSource: RemoteSource

    override suspend fun doWork(): Result {

        remoteSource = MovieClient.getInstance()

        settings = context.getSharedPreferences("Settings", AppCompatActivity.MODE_PRIVATE)
        alerts = context.getSharedPreferences("Alerts", AppCompatActivity.MODE_MULTI_PROCESS)
        val currentHour = (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)*60+Calendar.getInstance().get(Calendar.MINUTE)).toString()

        var days :Int = alerts.getInt(currentHour,0)

        val lang = settings.getString("language", "en").toString()
        val lat = settings.getString("lat", "-35").toString()
        val lon = settings.getString("lon", "151").toString()

        ///get data and show notifications

        val data = remoteSource.getWeather(lat, lon, lang)

        if (data.alert.isNullOrEmpty()) {
            Log.i("TAG", "doWork: null")

            showNotification("no alerts for today", " there is no alerts for today ,, Have a nice day ")
        } else {
            showNotification(data.alert!![0].event, data.alert!![0].description)
        }


        if (days > 0) {
            days--
            alerts.edit().putInt(currentHour, days).apply()

            ////set next alert
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val req: OneTimeWorkRequest = OneTimeWorkRequest
                .Builder(MyWorker::class.java)
                .addTag(currentHour)
                .setConstraints(constraints)
                .setInitialDelay(24*60, TimeUnit.MINUTES)
                .build()

            WorkManager.getInstance(context).enqueue(req)
            Log.i("TAG", "doWork: next alarm was set  :)")
        } else {
            //Clear alert from list
            alerts.edit().remove(currentHour).apply()
            Log.i("TAG", "doWork: alarm was cleared  :)")
        }

        return Result.success()
    }

    private fun showNotification(event: String, eventDescription: String) {

        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()
        val name = "45"
        val descriptionText = "555"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("6", name, importance).apply {
            description = descriptionText
            setSound(
                Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/raw/finger"),
                audioAttributes
            )
        }

        // Register the channel with the system
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(context, "6")
            .setSmallIcon(R.drawable.cloud)
            .setContentTitle(event)
            .setContentText(eventDescription)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(eventDescription)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(context)) {
            notify(6, builder.build())
        }
    }

}

