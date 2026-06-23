package com.example.edsweatherapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

//// object = singleton — only one RetrofitClient exists in the entire app
//object RetrofitClient {
//
//    // "by lazy" = create this only when it is first accessed
//    // If weatherApiService is never used, it's never created — saves memory
//    // Once created, the same instance is reused every time — no rebuilding
//    val weatherApiService: WeatherApiService by lazy {
//        Retrofit.Builder()
//            .baseUrl(AppConstants.WEATHER_BASE_URL)
//            // baseUrl = "https://api.openweathermap.org/"
//            // Retrofit prepends this to every @GET path in WeatherApiService
//            .addConverterFactory(GsonConverterFactory.create())
//            // GsonConverterFactory = automatic JSON → data class conversion
//            // When the API returns JSON, Gson maps it to WeatherResponse fields
//            .build()
//            .create(WeatherApiService::class.java)
//        // .create() = Retrofit generates the real HTTP implementation of our interface
//        // WeatherApiService::class.java = tells Retrofit which interface to implement
//    }
//
//    val feedbackApiService: FeedbackApiService by lazy{
//        Retrofit.Builder()
//            .baseUrl(AppConstants.FFEDBACK_BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(FeedbackApiService::class.java)
//    }
//}

class RetryInterceptor(private val maxRetries: Int=3) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var lastException: IOException? = null
        repeat(maxRetries){
            try{
                return chain.proceed(chain.request())
            } catch (e: okio.IOException) {
                lastException = e
            }
        }
        throw lastException ?: okio.IOException("Request failed after $maxRetries retries")
    }
}

object RetrofitClient {
    private lateinit var appContext: Context
    fun init(context: Context) {
        appContext = context.applicationContext
    }
    val weatherApiService : WeatherApiService by lazy {
        val cacheDir = File(appContext.cacheDir, "http_cache")
        val cache = Cache(cacheDir, 5L*1024*1024)
        val okHttpClient = OkHttpClient.Builder()
            .cache(cache)
            .addNetworkInterceptor { chain ->
                chain.proceed(chain.request()).newBuilder()
                    .header("Cache-Control", "public, max-age=300")
                    .build()
            }
            .addInterceptor(RetryInterceptor(maxRetries = 3))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        Retrofit.Builder()
            .baseUrl(AppConstants.WEATHER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }

    val feedbackApiService: FeedbackApiService by lazy{
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(RetryInterceptor(maxRetries = 3))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        Retrofit.Builder()
            .baseUrl(AppConstants.FEEDBACK_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FeedbackApiService::class.java)
    }
}