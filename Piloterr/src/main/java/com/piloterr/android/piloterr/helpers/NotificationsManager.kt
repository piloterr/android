package com.piloterr.android.piloterr.helpers

import android.content.Context
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.data.ApiClient
import com.piloterr.android.piloterr.events.ShowAchievementDialog
import com.piloterr.android.piloterr.events.ShowCheckinDialog
import com.piloterr.android.piloterr.events.ShowFirstDropDialog
import com.piloterr.android.piloterr.events.ShowSnackbarEvent
import com.piloterr.android.piloterr.models.Notification
import com.piloterr.android.piloterr.models.notifications.AchievementData
import com.piloterr.android.piloterr.models.notifications.FirstDropData
import com.piloterr.android.piloterr.models.notifications.LoginIncentiveData
import com.piloterr.android.piloterr.models.user.User
import com.piloterr.android.piloterr.ui.views.PiloterrSnackbar
import com.piloterr.android.piloterr.ui.views.dialogs.AchievementDialog
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject
import org.greenrobot.eventbus.EventBus
import java.util.*
import java.util.concurrent.TimeUnit

class NotificationsManager (private val context: Context) {
    private val seenNotifications: MutableMap<String, Boolean>
    private var apiClient: ApiClient? = null

    private val notifications: BehaviorSubject<List<Notification>>

    private var lastNotificationHandling: Date? = null

    init {
        this.seenNotifications = HashMap()
        this.notifications = BehaviorSubject.create()
    }

    fun setNotifications(current: List<Notification>) {
        this.notifications.onNext(current)

        this.handlePopupNotifications(current)
    }

    fun getNotifications(): Flowable<List<Notification>> {
        return this.notifications
                .startWith(emptyList<Notification>())
                .toFlowable(BackpressureStrategy.LATEST)
    }

    fun getNotification(id: String): Notification? {
        return this.notifications.value?.find { it.id == id }
    }

    fun setApiClient(apiClient: ApiClient?) {
        this.apiClient = apiClient
    }

    private fun handlePopupNotifications(notifications: List<Notification>): Boolean? {
        val now = Date()
        if (now.time - (lastNotificationHandling?.time ?: 0) < 300) {
            return true
        }
        lastNotificationHandling = now
        notifications
                .filter { !this.seenNotifications.containsKey(it.id) }
                .map {
                    val notificationDisplayed = when (it.type) {
                        Notification.Type.LOGIN_INCENTIVE.type -> displayLoginIncentiveNotification(it)
                        Notification.Type.ACHIEVEMENT_PARTY_UP.type -> displayAchievementNotification(it)
                        Notification.Type.ACHIEVEMENT_PARTY_ON.type -> displayAchievementNotification(it)
                        Notification.Type.ACHIEVEMENT_BEAST_MASTER.type -> displayAchievementNotification(it)
                        Notification.Type.ACHIEVEMENT_MOUNT_MASTER.type -> displayAchievementNotification(it)
                        Notification.Type.ACHIEVEMENT_TRIAD_BINGO.type -> displayAchievementNotification(it)
                        Notification.Type.ACHIEVEMENT_GUILD_JOINED.type -> displayAchievementNotification(it)
                        Notification.Type.ACHIEVEMENT_CHALLENGE_JOINED.type -> displayAchievementNotification(it)
                        Notification.Type.ACHIEVEMENT_INVITED_FRIEND.type -> displayAchievementNotification(it)
                        Notification.Type.ACHIEVEMENT_GENERIC.type -> displayAchievementNotification(it, notifications.find { notif ->
                            notif.type == Notification.Type.ACHIEVEMENT_ONBOARDING_COMPLETE.type
                        } != null)
                        Notification.Type.ACHIEVEMENT_ONBOARDING_COMPLETE.type -> displayAchievementNotification(it)
                        Notification.Type.FIRST_DROP.type -> displayFirstDropNotification(it)
                        else -> false
                    }

                    if (notificationDisplayed == true) {
                        this.seenNotifications[it.id] = true
                    }

                }

        return true
    }

    private fun displayFirstDropNotification(notification: Notification): Boolean {
        val data = (notification.data as? FirstDropData)
        EventBus.getDefault().post(ShowFirstDropDialog(data?.egg ?: "", data?.hatchingPotion ?: "", notification.id))
        return true
    }

    private fun displayLoginIncentiveNotification(notification: Notification): Boolean? {
        val notificationData = notification.data as? LoginIncentiveData
        val nextUnlockText = context.getString(R.string.nextPrizeUnlocks, notificationData?.nextRewardAt)
        if (notificationData?.rewardKey != null) {
            val event = ShowCheckinDialog(notification, nextUnlockText, notificationData.nextRewardAt ?: 0)
            EventBus.getDefault().post(event)
        } else {
            val event = ShowSnackbarEvent()
            event.title = notificationData?.message
            event.text = nextUnlockText
            event.type = PiloterrSnackbar.SnackbarDisplayType.BLUE
            EventBus.getDefault().post(event)
            if (apiClient != null) {
                apiClient?.readNotification(notification.id)
                        ?.subscribe(Consumer {}, RxErrorHandler.handleEmptyError())
            }
        }
        return true
    }

    private fun displayAchievementNotification(notification: Notification, isLastOnboardingAchievement: Boolean = false): Boolean {
        val achievement = (notification.data as? AchievementData)?.achievement ?: notification.type ?: ""
        val delay: Long = if (achievement == "createdTask" || achievement == Notification.Type.ACHIEVEMENT_ONBOARDING_COMPLETE.type) {
            1000
        } else {
            200
        }
        val sub = Completable.complete()
                .delay(delay, TimeUnit.MILLISECONDS)
                .subscribe(Action {
                    EventBus.getDefault().post(ShowAchievementDialog(achievement, notification.id, isLastOnboardingAchievement))
                }, RxErrorHandler.handleEmptyError())
        logOnboardingEvents(achievement)
        return true
    }

    private fun logOnboardingEvents(type: String) {
        if (User.ONBOARDING_ACHIEVEMENT_KEYS.contains(type)) {
            FirebaseAnalytics.getInstance(context).logEvent(type, null)
        } else if (type == Notification.Type.ACHIEVEMENT_ONBOARDING_COMPLETE.type) {
            FirebaseAnalytics.getInstance(context).logEvent(type, null)
        }
    }
}
