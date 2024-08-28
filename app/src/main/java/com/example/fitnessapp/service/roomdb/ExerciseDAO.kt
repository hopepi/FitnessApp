package com.example.fitnessapp.service.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fitnessapp.model.Exercise

@Dao
interface ExerciseDAO {

    @Insert
    suspend fun insertAll(vararg exercise : Exercise) : List<Long>

    @Query("SELECT * FROM exercise")
    suspend fun getAllExercise() : List<Exercise>

    @Query("SELECT * FROM exercise WHERE id = :exerciseId")
    suspend fun getExercise(exerciseId : Int) : Exercise

    @Query("DELETE FROM exercise")
    suspend fun deleteAllExercise()
}