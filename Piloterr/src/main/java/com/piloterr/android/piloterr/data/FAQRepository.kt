package com.piloterr.android.piloterr.data

import com.piloterr.android.piloterr.models.FAQArticle

import io.reactivex.Flowable
import io.realm.RealmResults

interface FAQRepository : BaseRepository {
    fun getArticles(): Flowable<RealmResults<FAQArticle>>
    fun getArticle(position: Int): Flowable<FAQArticle>
}
