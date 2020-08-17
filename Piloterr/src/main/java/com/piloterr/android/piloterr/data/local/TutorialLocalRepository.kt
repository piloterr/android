package com.piloterr.android.piloterr.data.local

import com.piloterr.android.piloterr.models.TutorialStep
import io.reactivex.Flowable
import io.realm.RealmResults

interface TutorialLocalRepository : BaseLocalRepository {

    fun getTutorialStep(key: String): Flowable<TutorialStep>
    fun getTutorialSteps(keys: List<String>): Flowable<RealmResults<TutorialStep>>
}
