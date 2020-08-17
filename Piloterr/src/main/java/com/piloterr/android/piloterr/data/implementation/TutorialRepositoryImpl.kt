package com.piloterr.android.piloterr.data.implementation

import com.piloterr.android.piloterr.data.ApiClient
import com.piloterr.android.piloterr.data.TutorialRepository
import com.piloterr.android.piloterr.data.local.TutorialLocalRepository
import com.piloterr.android.piloterr.models.TutorialStep
import io.reactivex.Flowable
import io.realm.RealmResults


class TutorialRepositoryImpl(localRepository: TutorialLocalRepository, apiClient: ApiClient, userID: String) : BaseRepositoryImpl<TutorialLocalRepository>(localRepository, apiClient, userID), TutorialRepository {

    override fun getTutorialStep(key: String): Flowable<TutorialStep> =
            localRepository.getTutorialStep(key)

    override fun getTutorialSteps(keys: List<String>): Flowable<RealmResults<TutorialStep>> =
            localRepository.getTutorialSteps(keys)
}
