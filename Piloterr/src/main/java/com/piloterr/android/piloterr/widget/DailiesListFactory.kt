package com.piloterr.android.piloterr.widget

import android.content.Context
import android.content.Intent

import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.models.tasks.Task


class DailiesListFactory(context: Context, intent: Intent) : TaskListFactory(context, intent, Task.TYPE_DAILY, R.layout.widget_dailies_list_row, R.id.dailies_text)