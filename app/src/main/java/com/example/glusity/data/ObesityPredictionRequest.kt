package com.example.glusity.data

data class ObesityPredictionRequest(
    val gender: Int,
    val age: Int,
    val height: Double,
    val weight: Double,
    val fcvc: Double,
    val ncp: Double,
    val ch2o: Double,
    val faf: Double,
    val tue: Double
)