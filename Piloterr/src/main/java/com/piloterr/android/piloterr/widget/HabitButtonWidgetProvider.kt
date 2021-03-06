package com.piloterr.android.piloterr.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.piloterr.android.piloterr.PiloterrBaseApplication
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.data.TaskRepository
import com.piloterr.android.piloterr.helpers.RxErrorHandler
import com.piloterr.android.piloterr.models.responses.TaskDirection
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import javax.inject.Inject

class HabitButtonWidgetProvider : BaseWidgetProvider() {
    @Inject
    lateinit var taskRepository: TaskRepository

    private fun setUp() {
        if (!hasInjected) {
            hasInjected = true
            PiloterrBaseApplication.userComponent?.inject(this)
        }
    }

    override fun layoutResourceId(): Int {
        return R.layout.widget_habit_button
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        setUp()
        val thisWidget = ComponentName(context,
                HabitButtonWidgetProvider::class.java)
        val allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)

        for (widgetId in allWidgetIds) {
            val options = appWidgetManager.getAppWidgetOptions(widgetId)
            appWidgetManager.partiallyUpdateAppWidget(widgetId,
                    sizeRemoteViews(context, options, widgetId))
        }

        // Build the intent to call the service
        val intent = Intent(context.applicationContext, HabitButtonWidgetService::class.java)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds)

        try {
            context.startService(intent)
        } catch (ignore: IllegalStateException) {
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        setUp()
        if (intent.action == HABIT_ACTION) {
            val mgr = AppWidgetManager.getInstance(context)
            val appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID)
            val taskId = intent.getStringExtra(TASK_ID)
            val direction = intent.getStringExtra(TASK_DIRECTION)

            val ids = intArrayOf(appWidgetId)

            if (taskId != null) {
                userRepository.getUser().firstElement().flatMap { user -> taskRepository.taskChecked(user, taskId, TaskDirection.UP.text == direction, false, null) }
                        .subscribe(Consumer { taskDirectionData -> showToastForTaskDirection(context, taskDirectionData) }, RxErrorHandler.handleEmptyError(), Action { this.onUpdate(context, mgr, ids) })
            }
        }
        super.onReceive(context, intent)
    }

    override fun configureRemoteViews(remoteViews: RemoteViews, widgetId: Int, columns: Int, rows: Int): RemoteViews {
        return remoteViews
    }

    companion object {

        const val HABIT_ACTION = "com.piloterr.android.piloterr.HABIT_ACTION"
        const val TASK_ID = "com.piloterr.android.piloterr.TASK_ID_ITEM"
        const val TASK_DIRECTION = "com.piloterr.android.piloterr.TASK_DIRECTION"
    }
}
