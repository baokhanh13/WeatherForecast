package com.example.weatherforecast.data.api

import com.example.weatherforecast.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    @GET("current.json")
    suspend fun searchWeather(
            @Query("q") query:String? = null,
            @Query("aqi") aqi: String = "no"
    ): WeatherResponse



    companion object {
        private const val BASE_URL = "https://api.weatherapi.com/v1/"

        fun create(): ApiService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .addInterceptor { chain ->
                        val url = chain.request().url.newBuilder().addQueryParameter("key", BuildConfig.API_KEY)
                                .build()
                        val request = chain.request().newBuilder().url(url).build()
                        chain.proceed(request)
                    }
                    .build()
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiService::class.java)
        }
    }
}