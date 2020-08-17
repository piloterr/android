package com.piloterr.android.piloterr.helpers.notifications

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.piloterr.android.piloterr.PiloterrApplication
import com.piloterr.android.piloterr.PiloterrBaseApplication
import com.piloterr.android.piloterr.components.UserComponent
import java.util.*
import javax.inject.Inject

class PiloterrFirebaseMessagingService : FirebaseMessagingService() {

    private val userComponent: UserComponent?
    get() = PiloterrBaseApplication.userComponent

    @Inject
    internal lateinit var pushNotificationManager: PushNotificationManager

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        userComponent?.inject(this)
        if (this::pushNotificationManager.isInitialized) {
            pushNotificationManager.displayNotification(remoteMessage)
        }
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        userComponent?.inject(this)
        val refreshedToken = FirebaseInstanceId.getInstance().token
        if (refreshedToken != null && this::pushNotificationManager.isInitialized) {
            pushNotificationManager.refreshedToken = refreshedToken
        }
    }
}