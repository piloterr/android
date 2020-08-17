package com.piloterr.android.piloterr.widget

import android.content.Context
import android.content.Intent

import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.models.tasks.Task

class TodoListFactory(context: Context, intent: Intent) : TaskListFactory(context, intent, Task.TYPE_TODO, R.layout.widget_todo_list_row, R.id.todo_text)
