package com.piloterr.android.piloterr.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.models.tasks.Task
import com.piloterr.android.piloterr.ui.helpers.bindView
import com.piloterr.android.piloterr.ui.views.PiloterrEmojiTextView
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import java.util.*


class SkillTasksRecyclerViewAdapter(data: OrderedRealmCollection<Task>?, autoUpdate: Boolean) : RealmRecyclerViewAdapter<Task, SkillTasksRecyclerViewAdapter.TaskViewHolder>(data, autoUpdate) {

    private val taskSelectionEvents = PublishSubject.create<Task>()

    override fun getItemId(position: Int): Long {
        if (data != null) {
            val task = data?.get(position)
            if (task?.id?.length == 36) {
                return UUID.fromString(task.id).mostSignificantBits
            }
        }
        return UUID.randomUUID().mostSignificantBits
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.skill_task_item_card, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        data?.let {
            holder.bindHolder(it[position])
        }
    }

    fun getTaskSelectionEvents(): Flowable<Task> {
        return taskSelectionEvents.toFlowable(BackpressureStrategy.DROP)
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var task: Task? = null

        private val titleTextView: PiloterrEmojiTextView by bindView(R.id.titleTextView)
        private val notesTextView: PiloterrEmojiTextView by bindView(R.id.notesTextView)
        private val rightBorderView: View by bindView(R.id.rightBorderView)

        init {
            itemView.setOnClickListener(this)
            itemView.isClickable = true
        }

        internal fun bindHolder(task: Task) {
            this.task = task
            titleTextView.text = task.markdownText { titleTextView.text = it }
            if (task.notes?.isEmpty() == true) {
                notesTextView.visibility = View.GONE
            } else {
                notesTextView.visibility = View.VISIBLE
                notesTextView.text = task.markdownNotes { notesTextView.text = it }
            }
            rightBorderView.setBackgroundResource(task.lightTaskColor)
        }

        override fun onClick(v: View) {
            if (v == itemView) {
                task?.let {
                    taskSelectionEvents.onNext(it)
                }
            }
        }
    }
}
