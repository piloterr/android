package com.piloterr.android.piloterr.ui.adapter.tasks

import com.piloterr.android.piloterr.models.responses.TaskDirection
import com.piloterr.android.piloterr.models.tasks.ChecklistItem
import com.piloterr.android.piloterr.models.tasks.Task
import io.reactivex.Flowable
import io.realm.OrderedRealmCollection

interface TaskRecyclerViewAdapter {
    var ignoreUpdates: Boolean

    val errorButtonEvents: Flowable<String>

    var taskDisplayMode: String

    fun updateData(tasks: OrderedRealmCollection<Task>?)

    fun filter()

    fun notifyItemMoved(adapterPosition: Int, adapterPosition1: Int)
    fun notifyDataSetChanged()
    fun getItemViewType(position: Int): Int
    fun getTaskIDAt(position: Int): String?

    fun updateUnfilteredData(data: OrderedRealmCollection<Task>?)

    val taskScoreEvents: Flowable<Pair<Task, TaskDirection>>
    val checklistItemScoreEvents: Flowable<Pair<Task, ChecklistItem>>
    val taskOpenEvents: Flowable<Task>
    val brokenTaskEvents: Flowable<Task>
}
