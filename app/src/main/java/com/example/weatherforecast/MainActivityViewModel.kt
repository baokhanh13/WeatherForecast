package com.example.weatherforecast

import androidx.lifecycle.ViewModel
import com.example.weatherforecast.data.api.WeatherResponse
import com.example.weatherforecast.data.repository.WeatherRepository
import com.example.weatherforecast.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val repository: WeatherRepository): ViewModel() {
    fun searchWeather(query: String? = null): Flow<Resource<WeatherResponse>> {
        return repository.searchWeather(query)
    }
}