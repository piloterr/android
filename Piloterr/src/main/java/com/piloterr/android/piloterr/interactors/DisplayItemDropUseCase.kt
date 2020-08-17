package com.piloterr.android.piloterr.interactors

import android.os.Handler
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.piloterr.android.piloterr.executors.PostExecutionThread
import com.piloterr.android.piloterr.executors.ThreadExecutor
import com.piloterr.android.piloterr.helpers.SoundManager
import com.piloterr.android.piloterr.models.responses.TaskScoringResult
import com.piloterr.android.piloterr.ui.views.PiloterrSnackbar
import io.reactivex.Flowable
import javax.inject.Inject

class DisplayItemDropUseCase @Inject
constructor(private val soundManager: SoundManager, threadExecutor: ThreadExecutor, postExecutionThread: PostExecutionThread) : UseCase<DisplayItemDropUseCase.RequestValues, Void>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(requestValues: RequestValues): Flowable<Void> {
        return Flowable.defer {
            val data = requestValues.data

            if (data?.drop != null) {
                Handler().postDelayed({
                    PiloterrSnackbar.showSnackbar(requestValues.snackbarTargetView,
                            data.drop?.dialog, PiloterrSnackbar.SnackbarDisplayType.DROP)
                    soundManager.loadAndPlayAudio(SoundManager.SoundItemDrop)
                }, 3000L)
            }

            Flowable.empty<Void>()
        }
    }

    class RequestValues(val data: TaskScoringResult?, val context: AppCompatActivity, val snackbarTargetView: ViewGroup) : UseCase.RequestValues
}