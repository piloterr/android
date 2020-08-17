package com.piloterr.android.piloterr.ui.adapter.tasks

import android.view.ViewGroup

import com.piloterr.android.piloterr.helpers.TaskFilterHelper
import com.piloterr.android.piloterr.models.tasks.Task
import com.piloterr.android.piloterr.ui.viewHolders.tasks.DailyViewHolder

import io.realm.OrderedRealmCollection

class DailiesRecyclerViewHolder(data: OrderedRealmCollection<Task>?, autoUpdate: Boolean, layoutResource: Int, taskFilterHelper: TaskFilterHelper) : RealmBaseTasksRecyclerViewAdapter<DailyViewHolder>(data, autoUpdate, layoutResource, taskFilterHelper) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder =
            DailyViewHolder(getContentView(parent), { task, direction -> taskScoreEventsSubject.onNext(Pair(task, direction)) },
                    { task, item -> checklistItemScoreSubject.onNext(Pair(task, item))}, {
                task -> taskOpenEventsSubject.onNext(task)
            }) {
                task -> brokenTaskEventsSubject.onNext(task)
            }
}
