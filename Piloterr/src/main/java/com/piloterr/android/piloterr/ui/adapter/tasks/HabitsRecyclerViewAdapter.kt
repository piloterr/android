package com.piloterr.android.piloterr.ui.adapter.tasks

import android.view.ViewGroup
import com.piloterr.android.piloterr.helpers.TaskFilterHelper
import com.piloterr.android.piloterr.models.tasks.Task
import com.piloterr.android.piloterr.ui.viewHolders.tasks.HabitViewHolder
import io.realm.OrderedRealmCollection

class HabitsRecyclerViewAdapter(data: OrderedRealmCollection<Task>?, autoUpdate: Boolean, layoutResource: Int, taskFilterHelper: TaskFilterHelper) : RealmBaseTasksRecyclerViewAdapter<HabitViewHolder>(data, autoUpdate, layoutResource, taskFilterHelper) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder =
            HabitViewHolder(getContentView(parent), { task, direction -> taskScoreEventsSubject.onNext(Pair(task, direction)) }, {
                task -> taskOpenEventsSubject.onNext(task)
            }) {
                task -> brokenTaskEventsSubject.onNext(task)
            }
}