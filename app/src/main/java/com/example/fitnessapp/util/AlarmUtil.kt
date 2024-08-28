package com.example.fitnessapp.util

import android.content.Context
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.concurrent.TimeUnit

fun scheduleNotification(context: Context, delayMinutes: Long) {
    val workRequest: WorkRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
        .setInitialDelay(delayMinutes, TimeUnit.MINUTES)
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
}
fun parseNotificationTime(notificationTime: String): LocalDateTime? {
    return try {
        val time = LocalTime.parse(notificationTime, DateTimeFormatter.ofPattern("HH:mm"))
        val today = LocalDateTime.now().toLocalDate()
        LocalDateTime.of(today, time)
    } catch (e: DateTimeParseException) {
        null
    }
}

fun calculateDelayInMinutes(scheduleTime: LocalDateTime): Long {
    val now = LocalDateTime.now()
    val scheduledDateTime = if (scheduleTime.isBefore(now)) {
        // Zaman geçmişse, bir sonraki gün için hesapla
        scheduleTime.plusDays(1)
    } else {
        scheduleTime
    }
    return Duration.between(now, scheduledDateTime).toMinutes()
}
