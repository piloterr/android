package com.piloterr.android.piloterr.data.implementation

import android.content.Context
import com.piloterr.android.piloterr.data.ApiClient
import com.piloterr.android.piloterr.data.ContentRepository
import com.piloterr.android.piloterr.data.local.ContentLocalRepository
import com.piloterr.android.piloterr.models.ContentResult
import com.piloterr.android.piloterr.models.WorldState
import com.piloterr.android.piloterr.models.inventory.SpecialItem
import io.reactivex.Flowable
import io.realm.RealmList
import java.util.*

abstract class ContentRepositoryImpl<T : ContentLocalRepository>(localRepository: T, apiClient: ApiClient) : BaseRepositoryImpl<T>(localRepository, apiClient), ContentRepository {

    private var lastContentSync = 0L
    private var lastWorldStateSync = 0L

    override fun retrieveContent(context: Context?): Flowable<ContentResult> {
        return retrieveContent(context,false)
    }

    override fun retrieveContent(context: Context?, forced: Boolean): Flowable<ContentResult> {
        val now = Date().time
        return if (forced || now - this.lastContentSync > 300000) {
            lastContentSync = now
            apiClient.content.doOnNext {
                context?.let {context ->
                    it.special = RealmList()
                    it.special.add(SpecialItem.makeMysteryItem(context))
                }
                localRepository.saveContent(it)
            }
        } else {
            Flowable.empty()
        }
    }

    override fun retrieveWorldState(): Flowable<WorldState> {
        val now = Date().time
        return if (now - this.lastWorldStateSync > 3600000) {
            lastWorldStateSync = now
            apiClient.worldState.doOnNext { localRepository.saveWorldState(it) }
        } else {
            Flowable.empty()
        }
    }
}
