package com.piloterr.android.piloterr.data.local

import com.piloterr.android.piloterr.models.Tag

import io.reactivex.Flowable
import io.realm.RealmResults

interface TagLocalRepository : BaseLocalRepository {
    fun getTags(userId: String): Flowable<RealmResults<Tag>>

    fun removeOldTags(onlineTags: List<Tag>, userID: String)
    fun deleteTag(tagID: String)
}
