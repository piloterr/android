package com.piloterr.android.piloterr.data

import com.piloterr.android.piloterr.models.TutorialStep
import io.reactivex.Flowable
import io.realm.RealmResults

interface TutorialRepository : BaseRepository {

    fun getTutorialStep(key: String): Flowable<TutorialStep>
    fun getTutorialSteps(keys: List<String>): Flowable<RealmResults<TutorialStep>>

}
