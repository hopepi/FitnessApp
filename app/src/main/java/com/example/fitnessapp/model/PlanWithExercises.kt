package com.example.fitnessapp.model

import androidx.room.Embedded
import androidx.room.Relation

data class PlanWithExercises(
    @Embedded val plan: Plans,
    @Relation(
        parentColumn = "planId",
        entityColumn = "planId"
    )
    val exercises: List<PlanExercise>
)
