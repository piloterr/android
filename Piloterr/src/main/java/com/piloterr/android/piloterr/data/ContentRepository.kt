package com.piloterr.android.piloterr.data

import android.content.Context
import com.piloterr.android.piloterr.models.ContentResult
import com.piloterr.android.piloterr.models.WorldState

import io.reactivex.Flowable

interface ContentRepository {

    fun retrieveContent(context: Context?): Flowable<ContentResult>
    fun retrieveContent(context: Context?, forced: Boolean): Flowable<ContentResult>

    fun retrieveWorldState(): Flowable<WorldState>
}
