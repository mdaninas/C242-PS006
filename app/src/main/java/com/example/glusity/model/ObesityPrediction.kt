package com.example.glusity.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "obesity_predictions")
data class ObesityPrediction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val bmi: Float,
    val risk: Float,
    val recommendations: String
)