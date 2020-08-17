package com.piloterr.android.piloterr.data.local

import com.piloterr.android.piloterr.models.FAQArticle

import io.reactivex.Flowable
import io.realm.RealmResults

interface FAQLocalRepository : ContentLocalRepository {
    fun getArticle(position: Int): Flowable<FAQArticle>

    val articles: Flowable<RealmResults<FAQArticle>>
}
