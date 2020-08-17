package com.piloterr.android.piloterr.data.implementation

import com.piloterr.android.piloterr.data.ApiClient
import com.piloterr.android.piloterr.data.FAQRepository
import com.piloterr.android.piloterr.data.local.FAQLocalRepository
import com.piloterr.android.piloterr.models.FAQArticle

import io.reactivex.Flowable
import io.realm.RealmResults


class FAQRepositoryImpl(localRepository: FAQLocalRepository, apiClient: ApiClient, userID: String) : BaseRepositoryImpl<FAQLocalRepository>(localRepository, apiClient, userID), FAQRepository {
    override fun getArticle(position: Int): Flowable<FAQArticle> {
        return localRepository.getArticle(position)
    }

    override fun getArticles(): Flowable<RealmResults<FAQArticle>> {
        return localRepository.articles
    }
}
