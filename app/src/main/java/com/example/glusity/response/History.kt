package com.example.glusity.response

data class History(
    val diabetes_predictions: List<DiabetesPrediction>,
    val obesity_predictions: List<ObesityPrediction>
)