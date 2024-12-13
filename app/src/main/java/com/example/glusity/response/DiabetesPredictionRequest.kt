package com.example.glusity.response

data class DiabetesPredictionRequest(
    val gender: Int,
    val age: Int,
    val height: Double,
    val weight: Double,
    val heart_disease: Int,
    val hypertension: Int,
    val smoking_history: Int,
    val blood_glucose_level: Int,
    val has_hba1c: Int,
    val hba1c: Double? = null
)