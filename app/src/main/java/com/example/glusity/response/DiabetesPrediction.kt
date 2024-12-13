package com.example.glusity.response

data class DiabetesPrediction(
    val age: Int,
    val blood_glucose: Double,
    val bmi: Double,
    val created_at: String,
    val gender: Int,
    val has_hba1c: Boolean,
    val hba1c_level: Double?,
    val heart_disease: Boolean,
    val height: Double,
    val hypertension: Boolean,
    val id: Int,
    val prediction: Double,
    val recommendation: String,
    val smoking_history: Boolean,
    val user_id: Int,
    val weight: Double
)