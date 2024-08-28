package com.example.fitnessapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "planExercises",
    foreignKeys = [ForeignKey(
        entity = Plans::class,
        parentColumns = ["planId"],
        childColumns = ["planId"],
        onDelete = ForeignKey.CASCADE//İlişkili tabloları silerken yardımcı olur
    )]
)
data class PlanExercise(
    @ColumnInfo(name = "planId")
    val planId : Int,
    @ColumnInfo(name = "exerciseId")
    val exerciseId : Int,
    @ColumnInfo(name = "quantity")
    val quantity : Int
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}
