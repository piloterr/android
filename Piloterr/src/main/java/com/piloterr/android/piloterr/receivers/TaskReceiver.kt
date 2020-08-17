package com.piloterr.android.piloterr.receivers

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.piloterr.android.piloterr.PiloterrBaseApplication
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.data.TaskRepository
import com.piloterr.android.piloterr.helpers.AmplitudeManager
import com.piloterr.android.piloterr.helpers.RxErrorHandler
import com.piloterr.android.piloterr.helpers.TaskAlarmManager
import com.piloterr.android.piloterr.models.tasks.Task
import com.piloterr.android.piloterr.ui.activities.MainActivity
import com.piloterr.shared.piloterr.HLogger
import com.piloterr.shared.piloterr.LogLevel
import io.reactivex.functions.Consumer
import java.util.*
import javax.inject.Inject




class TaskReceiver : BroadcastReceiver() {

    @Inject
    lateinit var taskAlarmManager: TaskAlarmManager

    @Inject
    lateinit var taskRepository: TaskRepository

    override fun onReceive(context: Context, intent: Intent) {
        HLogger.log(LogLevel.INFO, this::javaClass.name, "onReceive")
        PiloterrBaseApplication.userComponent?.inject(this)
        val extras = intent.extras
        if (extras != null) {
            val taskId = extras.getString(TaskAlarmManager.TASK_ID_INTENT_KEY)
            //This will set up the next reminders for dailies
            if (taskId != null) {
                taskAlarmManager.addAlarmForTaskId(taskId)
            }

            taskRepository.getTask(taskId ?: "")
                    .firstElement()
                    .subscribe(Consumer {
                        if (!it.isValid || it.completed) {
                            return@Consumer
                        }

                        val additionalData = HashMap<String, Any>()
                        additionalData["identifier"] = "task_reminder"
                        AmplitudeManager.sendEvent("receive notification", AmplitudeManager.EVENT_CATEGORY_BEHAVIOUR, AmplitudeManager.EVENT_HITTYPE_EVENT, additionalData)

                        createNotification(context, it)
                    }, RxErrorHandler.handleEmptyError())
        }
    }

    private fun createNotification(context: Context, task: Task) {
        val intent = Intent(context, MainActivity::class.java)
        HLogger.log(LogLevel.INFO, this::javaClass.name, "Create Notification")

        intent.putExtra("notificationIdentifier", "task_reminder")
        val pendingIntent = PendingIntent.getActivity(context, System.currentTimeMillis().toInt(), intent, 0)
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(context, "default")
                .setSmallIcon(R.drawable.ic_gryphon_white)
                .setContentTitle(task.text)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSound(soundUri)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        if (task.type == Task.TYPE_DAILY || task.type == Task.TYPE_TODO) {
            val completeIntent = Intent(context, LocalNotificationActionReceiver::class.java)
            completeIntent.action = context.getString(R.string.complete_task_action)
            completeIntent.putExtra("taskID", task.id)
            val pendingIntentComplete = PendingIntent.getBroadcast(
                    context,
                    3000,
                    completeIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
            notificationBuilder.addAction(0, context.getString(R.string.complete), pendingIntentComplete)
        }
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(task.id.hashCode(), notificationBuilder.build())
    }
}