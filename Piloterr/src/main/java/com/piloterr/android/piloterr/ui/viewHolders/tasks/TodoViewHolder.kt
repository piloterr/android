package com.piloterr.android.piloterr.ui.viewHolders.tasks

import android.view.View
import com.piloterr.android.piloterr.models.responses.TaskDirection
import com.piloterr.android.piloterr.models.tasks.ChecklistItem

import com.piloterr.android.piloterr.models.tasks.Task

import java.text.DateFormat

class TodoViewHolder(itemView: View, scoreTaskFunc: ((Task, TaskDirection) -> Unit), scoreChecklistItemFunc: ((Task, ChecklistItem) -> Unit), openTaskFunc: ((Task) -> Unit), brokenTaskFunc: ((Task) -> Unit)) : ChecklistedViewHolder(itemView, scoreTaskFunc, scoreChecklistItemFunc, openTaskFunc, brokenTaskFunc) {

    private val dateFormatter: DateFormat = android.text.format.DateFormat.getDateFormat(context)

    override fun bind(newTask: Task, position: Int, displayMode: String) {
        this.task = newTask
        if (newTask.completed) {
            checklistIndicatorWrapper.setBackgroundColor(taskGray)
        } else {
            checklistIndicatorWrapper.setBackgroundColor(newTask.lightTaskColor)
        }
        super.bind(newTask, position, displayMode)
    }

    override fun configureSpecialTaskTextView(task: Task) {
        if (task.dueDate != null) {
            this.specialTaskTextView?.text = dateFormatter.format(task.dueDate)
            this.specialTaskTextView?.visibility = View.VISIBLE
        } else {
            this.specialTaskTextView?.visibility = View.INVISIBLE
        }
    }

    override fun shouldDisplayAsActive(newTask: Task): Boolean {
        return !newTask.completed
    }
}
