package com.example.glusity.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "obesity_results")
data class ObesityResult(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val category: String,
    val recommendation: String
)