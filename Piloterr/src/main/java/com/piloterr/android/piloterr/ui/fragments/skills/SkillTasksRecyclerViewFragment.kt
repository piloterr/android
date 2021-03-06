package com.piloterr.android.piloterr.ui.fragments.skills

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.components.UserComponent
import com.piloterr.android.piloterr.data.TaskRepository
import com.piloterr.android.piloterr.extensions.inflate
import com.piloterr.android.piloterr.helpers.RxErrorHandler
import com.piloterr.android.piloterr.models.tasks.Task
import com.piloterr.android.piloterr.modules.AppModule
import com.piloterr.android.piloterr.ui.adapter.SkillTasksRecyclerViewAdapter
import com.piloterr.android.piloterr.ui.fragments.BaseFragment
import com.piloterr.android.piloterr.ui.helpers.bindView
import com.piloterr.android.piloterr.ui.helpers.resetViews
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Named

class SkillTasksRecyclerViewFragment : BaseFragment() {
    @Inject
    lateinit var taskRepository: TaskRepository
    @field:[Inject Named(AppModule.NAMED_USER_ID)]
    lateinit var userId: String
    var taskType: String? = null

    private val recyclerView: RecyclerView? by bindView(R.id.recyclerView)

    var adapter: SkillTasksRecyclerViewAdapter = SkillTasksRecyclerViewAdapter(null, true)
    internal var layoutManager: LinearLayoutManager? = null

    private val taskSelectionEvents = PublishSubject.create<Task>()

    fun getTaskSelectionEvents(): Flowable<Task> {
        return taskSelectionEvents.toFlowable(BackpressureStrategy.DROP)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return container?.inflate(R.layout.fragment_recyclerview)
    }

    override fun injectFragment(component: UserComponent) {
        component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resetViews()

        val layoutManager = LinearLayoutManager(context)
        recyclerView?.layoutManager = layoutManager

        adapter = SkillTasksRecyclerViewAdapter(null, true)
        compositeSubscription.add(adapter.getTaskSelectionEvents().subscribe(Consumer {
            taskSelectionEvents.onNext(it)
        }, RxErrorHandler.handleEmptyError()))
        recyclerView?.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        var tasks = taskRepository.getTasks(taskType ?: "", userId)
        if (taskType == Task.TYPE_TODO) {
            tasks = tasks.map { it.where().equalTo("completed", false).findAll() }
        }
        compositeSubscription.add(tasks.subscribe(Consumer {
            adapter.updateData(it)
        }, RxErrorHandler.handleEmptyError()))
    }
}
