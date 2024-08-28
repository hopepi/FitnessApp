package com.example.fitnessapp.service.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fitnessapp.model.Exercise
import com.example.fitnessapp.model.PlanExercise
import com.example.fitnessapp.model.Plans


@Database(entities = [Exercise::class,Plans::class,PlanExercise::class], version = 1, exportSchema = false)
abstract class ExerciseDatabase : RoomDatabase() {

    abstract fun exerciseDAO() : ExerciseDAO
    abstract fun plansDAO() : PlansDAO


    companion object{
        @Volatile
        private var INSTANCE : ExerciseDatabase? = null
        private val lock = Any()

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(lock){
            INSTANCE ?: createDatabase(context).also {
                INSTANCE = it
            }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ExerciseDatabase::class.java,
            "ExerciseDatabase"
        ).build()
    }
}