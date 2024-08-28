package com.example.fitnessapp.repository

import com.example.fitnessapp.model.ExerciseList
import com.example.fitnessapp.service.apiservice.ExerciseAPI
import com.example.fitnessapp.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class ExerciseRepository @Inject constructor(
    private val api : ExerciseAPI
){

    suspend fun getExerciseList() : Resource<ExerciseList>{
        val response = try {
            api.getExercise()
        } catch (e : Exception){
            return Resource.Error("Veriler Çekilemedi")
        }
        return Resource.Success(response)
    }
}