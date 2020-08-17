package com.piloterr.android.piloterr.interactors

import com.piloterr.android.piloterr.data.TaskRepository
import com.piloterr.android.piloterr.executors.PostExecutionThread
import com.piloterr.android.piloterr.executors.ThreadExecutor
import com.piloterr.android.piloterr.helpers.SoundManager
import com.piloterr.android.piloterr.models.responses.TaskScoringResult
import com.piloterr.android.piloterr.models.tasks.Task
import com.piloterr.android.piloterr.models.user.User

import javax.inject.Inject

import io.reactivex.Flowable

class BuyRewardUseCase @Inject
constructor(private val taskRepository: TaskRepository, private val soundManager: SoundManager,
            threadExecutor: ThreadExecutor, postExecutionThread: PostExecutionThread) : UseCase<BuyRewardUseCase.RequestValues, TaskScoringResult>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(requestValues: RequestValues): Flowable<TaskScoringResult?> {
        return taskRepository
                .taskChecked(requestValues.user, requestValues.task, false, false, requestValues.notifyFunc)
                .doOnNext { soundManager.loadAndPlayAudio(SoundManager.SoundReward) }
    }

    class RequestValues(internal val user: User?, val task: Task, val notifyFunc: (TaskScoringResult) -> Unit) : UseCase.RequestValues
}
