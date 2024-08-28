package com.example.fitnessapp.service.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.fitnessapp.model.PlanExercise
import com.example.fitnessapp.model.PlanWithExercises
import com.example.fitnessapp.model.Plans

@Dao
interface PlansDAO {
    @Insert
    suspend fun insertPlan(plan: Plans): Long

    @Update
    suspend fun updatePlan(plan: Plans)

    @Delete
    suspend fun deletePlan(plan: Plans)

    @Query("SELECT * FROM plans")
    suspend fun getAllPlans(): List<Plans>

    @Query("SELECT * FROM plans WHERE planId = :planId")
    suspend fun getPlan(planId: Int): Plans

    @Query("UPDATE plans SET availability = :availability WHERE planId = :planId")
    suspend fun updatePlanAvailability(planId: Int, availability: Boolean)



    @Insert
    suspend fun insertPlanExercise(planExercise: PlanExercise): Long

    @Update
    suspend fun updatePlanExercise(planExercise: PlanExercise)

    @Delete
    suspend fun deletePlanExercise(planExercise: PlanExercise)

    @Query("SELECT * FROM planExercises WHERE planId = :planId")
    suspend fun getExercisesForPlan(planId: Int): List<PlanExercise>

    @Query("SELECT * FROM planExercises WHERE planId = :planId AND exerciseId = :exerciseId")
    suspend fun getExercisesForPlanQuantity(planId: Int, exerciseId: Int): List<PlanExercise>




    @Transaction
    @Query("SELECT * FROM plans WHERE planId = :planId")
    suspend fun getPlanWithExercises(planId: Int): PlanWithExercises


    @Transaction
    @Query("SELECT * FROM plans")
    suspend fun getPlanWithExercisesAll(): List<PlanWithExercises>
}