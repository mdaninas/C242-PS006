package com.example.glusity.ui.history

data class HistoryItem(
    val id: Int,
    val predictionType: String,
    val predictionValue: Double,
    val date: String,
    val recommendation: String? = null
)