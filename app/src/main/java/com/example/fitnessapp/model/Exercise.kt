package com.example.fitnessapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity("exercise")
data class Exercise(
    @PrimaryKey
    @ColumnInfo("id")
    @SerializedName("id")
    var id: Int,
    @ColumnInfo("name")
    @SerializedName("name")
    val name: String,
    @ColumnInfo("description")
    @SerializedName("description")
    val description: String,
    @ColumnInfo("recommendation")
    @SerializedName("recommendation")
    val recommendation: String
)