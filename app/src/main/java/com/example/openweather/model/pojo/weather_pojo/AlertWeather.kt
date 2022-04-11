package com.example.openweather.model.pojo.weather_pojo

import com.google.gson.annotations.SerializedName

data class AlertWeather(

    @SerializedName("sender_name")
    var sender_name:String ,
    @SerializedName("event")
    var event:String ,
    @SerializedName("start")
    var start:Int ,
    @SerializedName("end")
    var end:Int ,
    @SerializedName("description")
    var description:String ,
)
