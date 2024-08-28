package com.example.fitnessapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("plans")
data class Plans(
    @ColumnInfo(name = "planName")
    val planName : String,
    @ColumnInfo(name = "notificationTime")
    val notificationTime : String,
    @ColumnInfo(name = "availability")
    var availability : Boolean
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "planId")
    var planId : Int = 0
}
