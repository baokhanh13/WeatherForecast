package com.example.weatherforecast.data.api

import com.google.gson.annotations.SerializedName

data class Current(
    @SerializedName("temp_c")
    val temperature: Double,
    val condition: Condition,
    @SerializedName("wind_mph")
    val wind: Double,
    val humidity: Int,
    @SerializedName("feelslike_c")
    val feelsLike: Double,
    @SerializedName("vis_km")
    val visibility: Double,
    val uv: Double
)

data class Condition(
    val text: String,
    val icon: String
) {
    val url: String get() = "https:$icon"
}