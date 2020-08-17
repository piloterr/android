package com.piloterr.android.piloterr.ui.adapter.tasks

import android.view.ViewGroup

import com.piloterr.android.piloterr.helpers.TaskFilterHelper
import com.piloterr.android.piloterr.models.tasks.Task
import com.piloterr.android.piloterr.ui.viewHolders.tasks.TodoViewHolder

import io.realm.OrderedRealmCollection

class TodosRecyclerViewAdapter(data: OrderedRealmCollection<Task>?, autoUpdate: Boolean, layoutResource: Int, taskFilterHelper: TaskFilterHelper) : RealmBaseTasksRecyclerViewAdapter<TodoViewHolder>(data, autoUpdate, layoutResource, taskFilterHelper) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder =
            TodoViewHolder(getContentView(parent), { task, direction -> taskScoreEventsSubject.onNext(Pair(task, direction)) },
                    { task, item -> checklistItemScoreSubject.onNext(Pair(task, item))}, {
                task -> taskOpenEventsSubject.onNext(task)
            }) {
                task -> brokenTaskEventsSubject.onNext(task)
            }

}
