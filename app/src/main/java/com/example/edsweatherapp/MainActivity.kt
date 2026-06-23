package com.example.edsweatherapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.edsweatherapp.databinding.ActivityMainBinding
import androidx.lifecycle.lifecycleScope
import com.example.edsweatherapp.network.AppConstants
import com.example.edsweatherapp.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private var currentCity: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RetrofitClient.init(this)




        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGetWeather.setOnClickListener {
            val city = binding.etCity.text.toString().trim()
            if (city.isEmpty()) {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            currentCity = city //saves city before fetch
            fetchWeather(city)
        }

        binding.btnSubmitFeedback.setOnClickListener {
            if(currentCity.isEmpty()){
                Toast.makeText(this, "Please fetch weather for a city first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val rating = binding.ratingBar.rating.toInt()
            val comment =  binding.etComment.text.toString().trim()
            if(comment.isEmpty()){
                Toast.makeText(this, "Please Leave a Comment", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            submitFeedback(currentCity, rating, comment)
        }
    }

    private fun fetchWeather(city: String) {
        // =======================================================
        // ASSIGNMENT 1 — Implement this function!
        // =======================================================
        // Steps to complete:
        // 1. Use lifecycleScope.launch { } to start a coroutine
        // 2. Inside it, use withContext(Dispatchers.IO) { } for the network call
        // 3. Call RetrofitClient.weatherApiService.getWeather(city, AppConstants.API_KEY, AppConstants.UNITS)
        // 4. Check if response.isSuccessful
        // 5. If YES: use response.body() to update binding.tvCity, binding.tvTemperature, binding.tvDescription
        // 6. If NO: show a Toast "City not found. Check the name and try again."
        // 7. Wrap everything in try { } catch (e: Exception) { } for network errors

        // ← DELETE this placeholder line when you implement the function:

        lifecycleScope.launch {
            //lifecyclescope: tying coroutine with this activity, when the activity is destroyed,
            //all coroutines are canceled
            try{
                val response = withContext(Dispatchers.IO){
                    RetrofitClient.weatherApiService.getWeather(
                        city = city,
                        apiKey = AppConstants.API_KEY,
                        units = AppConstants.UNITS
                    )
                }
                if (response.isSuccessful){
                    val weather = response.body()
                    if (weather != null){
                        binding.tvCity.text = "City: ${weather.name}"
                        binding.tvTemperature.text = "Temperature: ${weather.main.temp} C"
                        binding.tvDescription.text = "Description: ${weather.weather[0].description}"
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "City not found, Check the name and try again",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            } catch (e: Exception){
                Toast.makeText(
                    this@MainActivity,
                    "Network Error, check Connection",
                    Toast.LENGTH_SHORT
                )
            }
        }
        Toast.makeText(this, "Coming soon — weather for $city", Toast.LENGTH_SHORT).show()
    }


    private fun submitFeedback(city: String, rating: Int, comment: String){
        lifecycleScope.launch {
            try{}
            catch(e: Exception){}
            finally{}
        }
    }
}