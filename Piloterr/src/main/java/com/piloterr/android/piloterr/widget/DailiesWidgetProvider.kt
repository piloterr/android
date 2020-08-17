package com.piloterr.android.piloterr.widget

import com.piloterr.android.piloterr.R

class DailiesWidgetProvider : TaskListWidgetProvider() {
    override val serviceClass: Class<*>
        get() = DailiesWidgetService::class.java
    override val providerClass: Class<*>
        get() = DailiesWidgetProvider::class.java
    override val titleResId: Int
        get() = R.string.dailies
}

