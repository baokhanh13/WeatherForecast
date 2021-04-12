package com.example.weatherforecast.data.repository

import android.accounts.NetworkErrorException
import android.util.Log
import com.example.weatherforecast.data.api.ApiService
import com.example.weatherforecast.data.api.WeatherResponse
import com.example.weatherforecast.utils.Resource
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.Flow
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val service: ApiService) {
    fun searchWeather(query: String? = null) = flow<Resource<WeatherResponse>> {
        emit(Resource.Loading())
        try {
            val response = service.searchWeather(query)
            Log.d("Response", response.toString())
            emit(Resource.Success(response))
        } catch (e: IOException) {
            emit(Resource.Error(e))
        } catch (e: NetworkErrorException) {
            emit(Resource.Error(e))
        } catch (e: HttpException) {
            emit(Resource.Error(e))
        }
    }
}