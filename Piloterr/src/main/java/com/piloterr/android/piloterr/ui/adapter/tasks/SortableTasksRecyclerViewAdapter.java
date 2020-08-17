package com.piloterr.android.piloterr.ui.adapter.tasks;

import android.content.Context;

import androidx.annotation.Nullable;

import com.piloterr.android.piloterr.ui.helpers.ItemTouchHelperAdapter;
import com.piloterr.android.piloterr.ui.helpers.ItemTouchHelperDropCallback;
import com.piloterr.android.piloterr.helpers.TaskFilterHelper;
import com.piloterr.android.piloterr.models.tasks.Task;
import com.piloterr.android.piloterr.ui.viewHolders.BindableViewHolder;

import java.util.Collections;

/**
 * Created by ell on 7/21/16.
 */
public abstract class SortableTasksRecyclerViewAdapter<VH extends BindableViewHolder<Task>>
        extends BaseTasksRecyclerViewAdapter<VH> implements ItemTouchHelperAdapter, ItemTouchHelperDropCallback {

    private SortTasksCallback sortCallback;

    public SortableTasksRecyclerViewAdapter(String taskType, TaskFilterHelper taskFilterHelper, int layoutResource,
                                            Context newContext, String userID, @Nullable SortTasksCallback sortCallback) {
        super(taskType, taskFilterHelper, layoutResource, newContext, userID);
        this.sortCallback = sortCallback;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (getFilteredContent().size() <= fromPosition || getFilteredContent().size() <= toPosition) {
            return;
        }
        Collections.swap(getFilteredContent(), fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        //NO OP
    }

    @Override
    public void onDrop(int from, int to) {
        if (this.sortCallback != null && from != to) {
            this.sortCallback.onMove(getFilteredContent().get(to), from, to);
        }
    }

    public interface SortTasksCallback {
        void onMove(Task task, int from, int to);
    }
}
