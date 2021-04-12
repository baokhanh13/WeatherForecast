package com.example.weatherforecast

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.weatherforecast.databinding.ActivityMainBinding
import com.example.weatherforecast.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var searchJob: Job? = null
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.apply {
            textInputSearch.setOnEditorActionListener { v: TextView, i: Int, _: KeyEvent? ->
                if (i == EditorInfo.IME_ACTION_DONE) {
                    search(textInputSearch.text.toString())
                    val imm: InputMethodManager = v.context
                        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
                true
            }
        }
    }


    private fun search(query: String? = null) {
        searchJob?.cancel()

        searchJob = lifecycleScope.launch {
            viewModel.searchWeather(query).collect {
                when(it) {
                    is Resource.Loading -> {
                        binding.apply {
                            progressBar.isVisible = true
                            layoutTemp.isVisible = false
                            layoutInfo.isVisible = false
                            tvError.isVisible = false
                            tvLocation.isVisible = false
                        }

                    }
                    is Resource.Success -> {
                        binding.apply {
                            tvLocation.isVisible = true
                            tvError.isVisible = false
                            layoutTemp.isVisible = true
                            layoutInfo.isVisible = true
                            progressBar.isVisible = false
                            tvCondition.text = it.data?.current?.condition?.text.toString()
                            ("Feels like " + it.data?.current?.feelsLike.toString() + "\u00B0").also { tvFeel.text = it }
                            tvLocation.text = it.data?.location?.name
                            (it.data?.current?.humidity.toString() + "km").also { tvHumidity.text = it }
                            (it.data?.current?.temperature.toString() + "\u00B0").also { tvTemperature.text = it }
                            ("UV " + it.data?.current?.uv.toString()).also { tvUv.text = it }
                            (it.data?.current?.wind.toString() + "m/h").also { tvWind.text = it }
                            tvVisibility.text = it.data?.current?.visibility.toString()

                            Glide.with(this@MainActivity)
                                .load(it.data?.current?.condition?.url)
                                .into(imageCondition)
                        }
                    }
                    else -> {
                        binding.apply {
                            progressBar.isVisible = false
                            layoutTemp.isVisible = false
                            layoutInfo.isVisible = false
                            tvError.isVisible = true
                            tvLocation.isVisible = false
                        }
                    }
                }
            }
        }
    }
}