package com.example.edsweatherapp.network

import com.example.edsweatherapp.data.FeedbackRequest
import com.example.edsweatherapp.data.FeedbackResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST



// This is a menu for our post

interface FeedbackApiService {
    @POST("feedback")
    suspend fun submitFeedback(
        @Body request: FeedbackRequest
    ): Response<FeedbackResponse>
}