package com.example.edsweatherapp.data


// This data class represents the JSON body we send to API Dog
//Gson converts this kotlin object into JSON


data class FeedbackRequest (
    val city: String,
    val rating: Int,
    val comment: String
)

