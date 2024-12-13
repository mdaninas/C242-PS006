package com.example.glusity.response

data class Predictions(
    val category: String,
    val category_index: Int,
    val predictions: List<Double>
)