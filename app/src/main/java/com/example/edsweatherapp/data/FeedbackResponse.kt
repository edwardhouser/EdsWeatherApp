package com.example.edsweatherapp.data

// This data class represents what API Dog sends back after we post, it is null until values are received.
data class FeedbackResponse (
    val message: String? = null,
    val success: Boolean? = null
)