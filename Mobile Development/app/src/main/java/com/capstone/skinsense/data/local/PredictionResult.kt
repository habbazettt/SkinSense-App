package com.capstone.skinsense.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prediction_results")
data class PredictionResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imageUri: String,
    val result: String,
    val suggestion: String,
    val confidenceScore: Float?
)

