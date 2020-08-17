package com.piloterr.android.piloterr.helpers.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.annotation.CallSuper
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.ui.activities.MainActivity
import java.util.*

/**
 * Created by keithholliday on 6/28/16.
 */
abstract class PiloterrLocalNotification(protected var context: Context, protected var identifier: String) {

    protected var data: Map<String, String>? = null
    protected var title: String? = null
    protected var message: String? = null

    protected var notificationBuilder = NotificationCompat.Builder(context, "default")
            .setSmallIcon(R.drawable.ic_earth_white)
            .setAutoCancel(true)

    open fun configureNotificationBuilder(data: MutableMap<String, String>): NotificationCompat.Builder {
        val path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        return notificationBuilder
                .setSound(path)
    }

    @CallSuper
    open fun notifyLocally(title: String?, message: String?, data: MutableMap<String, String>) {
        this.title = title
        this.message = message

        var notificationBuilder = configureNotificationBuilder(data)

        if (this.title != null) {
            notificationBuilder = notificationBuilder.setContentTitle(title)
        }
        if (this.message != null) {
            notificationBuilder = notificationBuilder.setContentText(message)
        }

        this.setNotificationActions(data)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(getNotificationID(data), notificationBuilder.build())
    }

    fun setExtras(data: Map<String, String>) {
        this.data = data
    }

    protected open fun setNotificationActions(data: Map<String, String>)  {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("notificationIdentifier", identifier)
        configureMainIntent(intent)
        val pendingIntent = PendingIntent.getActivity(
                context,
                3000,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        notificationBuilder.setContentIntent(pendingIntent)
    }

    protected open fun configureMainIntent(intent: Intent) {
    }

    protected open fun getNotificationID(data: MutableMap<String, String>): Int {
        return Date().time.toInt()
    }
}
