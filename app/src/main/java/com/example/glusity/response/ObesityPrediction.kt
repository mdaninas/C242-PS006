package com.example.glusity.response

data class ObesityPrediction(
    val age: Int,
    val ch2o: Int,
    val created_at: String,
    val faf: Int,
    val fcvc: Int,
    val gender: Int,
    val height: Double,
    val id: Int,
    val ncp: Int,
    val predicted_category: String,
    val predictions: Predictions,
    val recommendation: String,
    val tue: Int,
    val user_id: Int,
    val weight: Double
)