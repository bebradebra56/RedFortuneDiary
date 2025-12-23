package com.redifor.diarysof.utils

import android.content.Context
import androidx.work.*
import com.redifor.diarysof.workers.ReminderWorker
import java.util.concurrent.TimeUnit
import java.util.Calendar

object ReminderScheduler {
    
    fun scheduleReminder(context: Context, hour: Int, minute: Int) {
        val currentTime = Calendar.getInstance()
        val reminderTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }
        
        // If the time has already passed today, schedule for tomorrow
        if (reminderTime.before(currentTime)) {
            reminderTime.add(Calendar.DAY_OF_YEAR, 1)
        }
        
        val delay = reminderTime.timeInMillis - currentTime.timeInMillis
        
        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .addTag("daily_reminder")
            .build()
        
        WorkManager.getInstance(context).enqueueUniqueWork(
            "daily_reminder",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
    
    fun cancelReminder(context: Context) {
        WorkManager.getInstance(context).cancelAllWorkByTag("daily_reminder")
    }
}

