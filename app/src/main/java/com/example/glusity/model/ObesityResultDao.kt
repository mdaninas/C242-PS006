package com.example.glusity.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ObesityResultDao {

    @Insert
    suspend fun insertResult(obesityResult: ObesityResult)

    @Query("SELECT * FROM obesity_results")
    suspend fun getAllResults(): List<ObesityResult>
}