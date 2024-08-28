package com.example.fitnessapp.service.apiservice

import com.example.fitnessapp.model.ExerciseList
import com.example.fitnessapp.util.Constans.END_POINT
import retrofit2.http.GET

interface ExerciseAPI {

    @GET(END_POINT)
    suspend fun getExercise() : ExerciseList
}