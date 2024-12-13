package com.example.glusity.response

data class DiabetesPredictionResponse(
    val bmi: Double,
    val hba1c: Double,
    val id: Int,
    val prediction: Double,
    val recommendation: String,
    val success: Boolean
)