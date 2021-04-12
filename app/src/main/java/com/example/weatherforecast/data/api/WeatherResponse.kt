package com.example.weatherforecast.data.api

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val location: Location,
    val current: Current
)