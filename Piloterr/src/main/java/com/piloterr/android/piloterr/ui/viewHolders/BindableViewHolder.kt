package com.piloterr.android.piloterr.ui.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BindableViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(data: T, position: Int, displayMode: String)
}