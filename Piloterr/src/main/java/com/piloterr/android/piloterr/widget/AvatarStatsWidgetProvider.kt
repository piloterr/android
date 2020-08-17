package com.piloterr.android.piloterr.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import com.piloterr.android.piloterr.PiloterrBaseApplication
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.helpers.HealthFormatter
import com.piloterr.android.piloterr.helpers.NumberAbbreviator
import com.piloterr.android.piloterr.helpers.RxErrorHandler
import com.piloterr.android.piloterr.models.user.User
import com.piloterr.android.piloterr.ui.AvatarView
import com.piloterr.android.piloterr.ui.activities.MainActivity
import com.piloterr.android.piloterr.ui.views.PiloterrIconsHelper
import io.reactivex.functions.Consumer

class AvatarStatsWidgetProvider : BaseWidgetProvider() {

    private var appWidgetManager: AppWidgetManager? = null

    private var showManaBar: Boolean = true

    override fun layoutResourceId(): Int {
        return R.layout.widget_avatar_stats
    }

    private fun setUp() {
        if (!hasInjected) {
            hasInjected = true
            PiloterrBaseApplication.userComponent?.inject(this)
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        this.setUp()
        this.appWidgetManager = appWidgetManager
        this.context = context

        userRepository.getUser().firstElement()?.subscribe(Consumer<User> { this.updateData(it) }, RxErrorHandler.handleEmptyError())
    }

    override fun configureRemoteViews(remoteViews: RemoteViews, widgetId: Int, columns: Int, rows: Int): RemoteViews {
        if (columns > 3) {
            remoteViews.setViewVisibility(R.id.avatar_view, View.VISIBLE)
        } else {
            remoteViews.setViewVisibility(R.id.avatar_view, View.GONE)
        }

        showManaBar = rows > 1
        if (rows > 1) {
            remoteViews.setViewVisibility(R.id.mp_wrapper, View.VISIBLE)
            remoteViews.setViewVisibility(R.id.detail_info_view, View.VISIBLE)
        } else {
            remoteViews.setViewVisibility(R.id.mp_wrapper, View.GONE)
            remoteViews.setViewVisibility(R.id.detail_info_view, View.GONE)
        }

        return remoteViews
    }

    private fun updateData(user: User?) {
        val context = context
        val appWidgetManager = appWidgetManager
        val stats = user?.stats
        if (user == null || stats == null || context == null || appWidgetManager == null) {
            return
        }
        val thisWidget = ComponentName(context, AvatarStatsWidgetProvider::class.java)
        val allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
        val currentHealth = HealthFormatter.format(stats.hp ?: 0.0)
        val currentHealthString = HealthFormatter.formatToString(stats.hp ?: 0.0)
        val healthValueString = currentHealthString + "/" + stats.maxHealth
        val expValueString = "" + stats.exp?.toInt() + "/" + stats.toNextLevel
        val mpValueString = "" + stats.mp?.toInt() + "/" + stats.maxMP

        for (widgetId in allWidgetIds) {
            var remoteViews = RemoteViews(context.packageName, R.layout.widget_avatar_stats)

            remoteViews.setTextViewText(R.id.TV_hp_value, healthValueString)
            remoteViews.setTextViewText(R.id.exp_TV_value, expValueString)
            remoteViews.setTextViewText(R.id.mp_TV_value, mpValueString)

            remoteViews.setImageViewBitmap(R.id.ic_hp_header, PiloterrIconsHelper.imageOfHeartDarkBg())
            remoteViews.setImageViewBitmap(R.id.ic_exp_header, PiloterrIconsHelper.imageOfExperience())
            remoteViews.setImageViewBitmap(R.id.ic_mp_header, PiloterrIconsHelper.imageOfMagic())

            remoteViews.setProgressBar(R.id.hp_bar, stats.maxHealth ?: 0, currentHealth.toInt(), false)
            remoteViews.setProgressBar(R.id.exp_bar, stats.toNextLevel ?: 0, stats.exp?.toInt() ?: 0, false)
            remoteViews.setProgressBar(R.id.mp_bar, stats.maxMP ?: 0, stats.mp?.toInt() ?: 0, false)
            remoteViews.setViewVisibility(R.id.mp_wrapper, if (showManaBar && (stats.habitClass == null || (stats.lvl ?: 0) < 10 || user.preferences?.disableClasses == true)) View.GONE else View.VISIBLE)

            remoteViews.setTextViewText(R.id.gold_tv, NumberAbbreviator.abbreviate(context, stats.gp ?: 0.0))
            remoteViews.setTextViewText(R.id.gems_tv, (user.balance * 4).toInt().toString())
            val hourGlassCount = user.hourglassCount
            if (hourGlassCount == 0) {
                remoteViews.setViewVisibility(R.id.hourglass_icon, View.GONE)
                remoteViews.setViewVisibility(R.id.hourglasses_tv, View.GONE)
            } else {
                remoteViews.setImageViewBitmap(R.id.hourglass_icon, PiloterrIconsHelper.imageOfHourglass())
                remoteViews.setViewVisibility(R.id.hourglass_icon, View.VISIBLE)
                remoteViews.setTextViewText(R.id.hourglasses_tv, hourGlassCount.toString())
                remoteViews.setViewVisibility(R.id.hourglasses_tv, View.VISIBLE)
            }
            remoteViews.setImageViewBitmap(R.id.gem_icon, PiloterrIconsHelper.imageOfGem())
            remoteViews.setImageViewBitmap(R.id.gold_icon, PiloterrIconsHelper.imageOfGold())
            remoteViews.setTextViewText(R.id.lvl_tv, context.getString(R.string.user_level, user.stats?.lvl ?: 0))

            val avatarView = AvatarView(context, showBackground = true, showMount = true, showPet = true)

            avatarView.setAvatar(user)
            val finalRemoteViews = remoteViews
            avatarView.onAvatarImageReady(Consumer { bitmap ->
                finalRemoteViews.setImageViewBitmap(R.id.avatar_view, bitmap)
                appWidgetManager.partiallyUpdateAppWidget(allWidgetIds, finalRemoteViews)
            })

            //If user click on life and xp: open the app
            val openAppIntent = Intent(context.applicationContext, MainActivity::class.java)
            val openApp = PendingIntent.getActivity(context, 0, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            remoteViews.setOnClickPendingIntent(R.id.widget_main_view, openApp)


            val options = appWidgetManager.getAppWidgetOptions(widgetId)
            remoteViews = sizeRemoteViews(context, options, widgetId)

            appWidgetManager.updateAppWidget(widgetId, remoteViews)
        }
    }
}
