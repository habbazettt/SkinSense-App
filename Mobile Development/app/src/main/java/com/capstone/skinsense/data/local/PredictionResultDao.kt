package com.capstone.skinsense.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PredictionResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(predictionResult: PredictionResult)

    @Query("SELECT * FROM prediction_results ORDER BY id DESC")
    fun getAllResults(): LiveData<List<PredictionResult>>
}
