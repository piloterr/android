package com.piloterr.android.piloterr.ui.activities

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.components.UserComponent
import com.piloterr.android.piloterr.data.TaskRepository
import com.piloterr.android.piloterr.helpers.RxErrorHandler
import com.piloterr.android.piloterr.models.tasks.Task
import com.piloterr.android.piloterr.modules.AppModule
import com.piloterr.android.piloterr.ui.adapter.SkillTasksRecyclerViewAdapter
import com.piloterr.android.piloterr.ui.helpers.bindView
import com.piloterr.android.piloterr.widget.HabitButtonWidgetProvider
import io.reactivex.functions.Consumer
import javax.inject.Inject
import javax.inject.Named

class HabitButtonWidgetActivity : BaseActivity() {

    @Inject
    lateinit var taskRepository: TaskRepository
    @field:[Inject Named(AppModule.NAMED_USER_ID)]
    lateinit var userId: String

    internal val recyclerView: RecyclerView by bindView(R.id.recyclerView)
    private var widgetId: Int = 0
    private var adapter: SkillTasksRecyclerViewAdapter? = null

    override fun getLayoutResId(): Int {
        return R.layout.widget_configure_habit_button
    }

    override fun injectActivity(component: UserComponent?) {
        component?.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        }

        // If this activity was started with an intent without an app widget ID,
        // finish with an error.
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
        }

        var layoutManager: LinearLayoutManager? = recyclerView.layoutManager as? LinearLayoutManager

        if (layoutManager == null) {
            layoutManager = LinearLayoutManager(this)

            recyclerView.layoutManager = layoutManager
        }

        adapter = SkillTasksRecyclerViewAdapter(null, true)
        adapter?.getTaskSelectionEvents()?.subscribe(Consumer { task -> taskSelected(task.id) },
                RxErrorHandler.handleEmptyError())
                ?.let { compositeSubscription.add(it) }
        recyclerView.adapter = adapter

        compositeSubscription.add(taskRepository.getTasks(Task.TYPE_HABIT, userId).firstElement().subscribe(Consumer { adapter?.updateData(it) }, RxErrorHandler.handleEmptyError()))
    }

    private fun taskSelected(taskId: String?) {
        finishWithSelection(taskId)
    }

    private fun finishWithSelection(selectedTaskId: String?) {
        storeSelectedTaskId(selectedTaskId)

        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        setResult(Activity.RESULT_OK, resultValue)
        finish()

        val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE, null, this, HabitButtonWidgetProvider::class.java)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(widgetId))
        sendBroadcast(intent)
    }

    private fun storeSelectedTaskId(selectedTaskId: String?) {
        PreferenceManager.getDefaultSharedPreferences(this).edit {
            putString("habit_button_widget_$widgetId", selectedTaskId)
        }
    }
}
