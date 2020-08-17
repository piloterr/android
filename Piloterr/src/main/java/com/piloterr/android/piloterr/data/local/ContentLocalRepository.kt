package com.piloterr.android.piloterr.data.local

import com.piloterr.android.piloterr.models.ContentResult
import com.piloterr.android.piloterr.models.WorldState

interface ContentLocalRepository : BaseLocalRepository {
    fun saveContent(contentResult: ContentResult)
    fun saveWorldState(worldState: WorldState)
}
