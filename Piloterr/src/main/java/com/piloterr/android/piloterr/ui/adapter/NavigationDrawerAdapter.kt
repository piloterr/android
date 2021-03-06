package com.piloterr.android.piloterr.ui.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.extensions.dpToPx
import com.piloterr.android.piloterr.extensions.inflate
import com.piloterr.android.piloterr.ui.helpers.bindOptionalView
import com.piloterr.android.piloterr.ui.menu.PiloterrDrawerItem
import com.piloterr.android.piloterr.ui.viewHolders.GiftOneGetOnePromoMenuView
import com.piloterr.android.piloterr.ui.views.adventureGuide.AdventureGuideMenuBanner
import com.piloterr.android.piloterr.ui.views.promo.SubscriptionBuyGemsPromoView
import com.piloterr.android.piloterr.ui.views.promo.SubscriptionBuyGemsPromoViewHolder
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject


class NavigationDrawerAdapter(tintColor: Int, backgroundTintColor: Int): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var tintColor: Int = tintColor
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var backgroundTintColor: Int = backgroundTintColor
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    internal val items: MutableList<PiloterrDrawerItem> = ArrayList()
    var selectedItem: Int? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val itemSelectedEvents = PublishSubject.create<PiloterrDrawerItem>()


    fun getItemSelectionEvents(): Flowable<PiloterrDrawerItem> = itemSelectedEvents.toFlowable(BackpressureStrategy.DROP)

    fun getItemWithTransitionId(transitionId: Int): PiloterrDrawerItem? =
            items.find { it.transitionId == transitionId }
    fun getItemWithIdentifier(identifier: String): PiloterrDrawerItem? =
            items.find { it.identifier == identifier }

    private fun getItemPosition(transitionId: Int): Int =
            items.indexOfFirst { it.transitionId == transitionId }
    private fun getItemPosition(identifier: String): Int =
            items.indexOfFirst { it.identifier == identifier }

    fun updateItem(item: PiloterrDrawerItem) {
        val position = getItemPosition(item.identifier)
        items[position] = item
        notifyDataSetChanged()
    }

    fun updateItems(newItems: List<PiloterrDrawerItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val drawerItem = getItem(position)
        when {
            getItemViewType(position) == 0 -> {
                val itemHolder = holder as? DrawerItemViewHolder
                itemHolder?.tintColor = tintColor
                itemHolder?.backgroundTintColor = backgroundTintColor
                itemHolder?.bind(drawerItem, drawerItem.transitionId == selectedItem)
                itemHolder?.itemView?.setOnClickListener { itemSelectedEvents.onNext(drawerItem) }
            }
            getItemViewType(position) == 1 -> {
                (holder as? SectionHeaderViewHolder)?.backgroundTintColor = backgroundTintColor
                (holder as? SectionHeaderViewHolder)?.bind(drawerItem)
            }
            getItemViewType(position) == 4 -> {
                drawerItem.user?.let { (holder.itemView as? AdventureGuideMenuBanner)?.updateData(it) }
                holder.itemView.setOnClickListener { itemSelectedEvents.onNext(drawerItem) }

            }
        }
    }

    private fun getItem(position: Int) = items.filter { it.isVisible }[position]

    override fun getItemCount(): Int = items.count { it.isVisible }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isHeader) {
            1
        } else {
            getItem(position).itemViewType ?: 0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            2 -> {
                val itemView = SubscriptionBuyGemsPromoView(parent.context)
                itemView.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        148.dpToPx(parent.context)
                )
                SubscriptionBuyGemsPromoViewHolder(itemView)
            }
            3 -> {
                val itemView = GiftOneGetOnePromoMenuView(parent.context)
                itemView.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        148.dpToPx(parent.context)
                )
                SubscriptionBuyGemsPromoViewHolder(itemView)
            }
            4 -> {
                val itemView = AdventureGuideMenuBanner(parent.context)
                itemView.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        104.dpToPx(parent.context)
                )
                SubscriptionBuyGemsPromoViewHolder(itemView)
            }
            1 -> SectionHeaderViewHolder(parent.inflate(R.layout.drawer_main_section_header))
            else -> DrawerItemViewHolder(parent.inflate(R.layout.drawer_main_item))
        }
    }

    class DrawerItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tintColor: Int = 0
        var backgroundTintColor: Int = 0

        private val titleTextView: TextView? by bindOptionalView(itemView, R.id.titleTextView)
        private val pillView: TextView? by bindOptionalView(itemView, R.id.pillView)
        private val bubbleView: View? by bindOptionalView(itemView, R.id.bubble_view)
        private val additionalInfoView: TextView? by bindOptionalView(itemView, R.id.additionalInfoView)

        fun bind(drawerItem: PiloterrDrawerItem, isSelected: Boolean) {
            titleTextView?.text = drawerItem.text

            if (isSelected) {
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.gray_600))
                titleTextView?.setTextColor(tintColor)
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.white))
                titleTextView?.setTextColor(ContextCompat.getColor(itemView.context, R.color.gray_10))
            }

            if (drawerItem.pillText != null) {
                pillView?.let { pillView ->
                    pillView.visibility = View.VISIBLE
                    pillView.text = drawerItem.pillText

                    val pL = pillView.paddingLeft
                    val pT = pillView.paddingTop
                    val pR = pillView.paddingRight
                    val pB = pillView.paddingBottom

                    pillView.background = drawerItem.pillBackground ?: ContextCompat.getDrawable(itemView.context, R.drawable.pill_bg_purple_200)
                    pillView.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                    pillView.setPadding(pL, pT, pR, pB)
                }
            } else {
                pillView?.visibility = View.GONE
            }
            if (drawerItem.subtitle != null){
                additionalInfoView?.let { additionalInfoView ->
                    additionalInfoView.text = drawerItem.subtitle
                    additionalInfoView.visibility = View.VISIBLE
                    additionalInfoView.setTextColor(drawerItem.subtitleTextColor ?: tintColor)
                }
            } else {
                additionalInfoView?.visibility = View.GONE
            }
            bubbleView?.visibility = if (drawerItem.showBubble) View.VISIBLE else View.GONE
        }
    }

    class SectionHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var backgroundTintColor: Int = 0

        fun bind(drawerItem: PiloterrDrawerItem) {
            (itemView as? TextView)?.text = drawerItem.text
            itemView.setBackgroundColor(backgroundTintColor)
        }
    }
}