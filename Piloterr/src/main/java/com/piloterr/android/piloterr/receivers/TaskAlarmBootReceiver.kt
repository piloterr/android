package com.piloterr.android.piloterr.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.piloterr.android.piloterr.PiloterrBaseApplication
import com.piloterr.android.piloterr.helpers.TaskAlarmManager
import com.piloterr.shared.piloterr.HLogger
import com.piloterr.shared.piloterr.LogLevel
import javax.inject.Inject

class TaskAlarmBootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var taskAlarmManager: TaskAlarmManager
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onReceive(context: Context, arg1: Intent) {
        PiloterrBaseApplication.userComponent?.inject(this)
        taskAlarmManager.scheduleAllSavedAlarms(sharedPreferences.getBoolean("preventDailyReminder", false))
        HLogger.log(LogLevel.INFO, this::javaClass.name, "onReceive")
    }

}
