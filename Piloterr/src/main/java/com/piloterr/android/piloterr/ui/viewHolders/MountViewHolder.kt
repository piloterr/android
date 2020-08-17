package com.piloterr.android.piloterr.ui.viewHolders

import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.ViewGroup
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.databinding.MountOverviewItemBinding
import com.piloterr.android.piloterr.extensions.inflate
import com.piloterr.android.piloterr.helpers.RxErrorHandler
import com.piloterr.android.piloterr.models.inventory.Mount
import com.piloterr.android.piloterr.ui.helpers.DataBindingUtils
import com.piloterr.android.piloterr.ui.menu.BottomSheetMenu
import com.piloterr.android.piloterr.ui.menu.BottomSheetMenuItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

class MountViewHolder(parent: ViewGroup, private val equipEvents: PublishSubject<String>) : androidx.recyclerview.widget.RecyclerView.ViewHolder(parent.inflate(R.layout.mount_overview_item)), View.OnClickListener {
    private var binding: MountOverviewItemBinding = MountOverviewItemBinding.bind(itemView)
    private var owned: Boolean = false
    var animal: Mount? = null

    var resources: Resources = itemView.resources

    init {
        itemView.setOnClickListener(this)
    }

    fun bind(item: Mount, owned: Boolean) {
        animal = item
        this.owned = owned
        binding.titleTextView.visibility = View.GONE
        binding.ownedTextView.visibility = View.GONE
        val imageName = "stable_Mount_Icon_" + item.animal + "-" + item.color
        binding.imageView.alpha = 1.0f
        if (!owned) {
            binding.imageView.alpha = 0.2f
        }
        binding.imageView.background = null
        DataBindingUtils.loadImage(imageName) {
            val drawable = BitmapDrawable(itemView.context.resources, if (owned) it else it.extractAlpha())
            Observable.just(drawable)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        binding.imageView.background = drawable
                    }, RxErrorHandler.handleEmptyError())
        }
    }

    override fun onClick(v: View) {
        if (!owned) {
            return
        }
        val menu = BottomSheetMenu(itemView.context)
        menu.setTitle(animal?.text)
        menu.addMenuItem(BottomSheetMenuItem(resources.getString(R.string.equip)))
        menu.setSelectionRunnable {
            animal?.let { equipEvents.onNext(it.key) }
        }
        menu.show()
    }
}