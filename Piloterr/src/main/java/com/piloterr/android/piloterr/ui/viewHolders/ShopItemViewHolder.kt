package com.piloterr.android.piloterr.ui.viewHolders

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.piloterr.android.piloterr.PiloterrBaseApplication
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.models.shops.ShopItem
import com.piloterr.android.piloterr.ui.helpers.DataBindingUtils
import com.piloterr.android.piloterr.ui.helpers.bindView
import com.piloterr.android.piloterr.ui.views.CurrencyView
import com.piloterr.android.piloterr.ui.views.PiloterrIconsHelper
import com.piloterr.android.piloterr.ui.views.shops.PurchaseDialog

class ShopItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private val imageView: SimpleDraweeView by bindView(itemView, R.id.imageView)
    private val buyButton: View by bindView(itemView, R.id.buyButton)
    private val priceLabel: CurrencyView by bindView(itemView, R.id.priceLabel)
    private val unlockLabel: TextView by bindView(itemView, R.id.unlockLabel)
    private val itemDetailIndicator: TextView by bindView(itemView, R.id.item_detail_indicator)
    private val pinIndicator: ImageView by bindView(itemView, R.id.pin_indicator)

    var shopIdentifier: String? = null
    private var item: ShopItem? = null

    private var context: Context = itemView.context

    private var lockedDrawable = BitmapDrawable(context.resources, PiloterrIconsHelper.imageOfItemIndicatorLocked())
    private var limitedDrawable = BitmapDrawable(context.resources, PiloterrIconsHelper.imageOfItemIndicatorLimited())
    private var countDrawable = BitmapDrawable(context.resources, PiloterrIconsHelper.imageOfItemIndicatorNumber())

    var purchaseCardAction: ((ShopItem) -> Unit)? = null

    var itemCount = 0
    set(value) {
        field = value
        if (value > 0) {
            itemDetailIndicator.text = value.toString()
            itemDetailIndicator.background = countDrawable
            itemDetailIndicator.visibility = View.VISIBLE
        }
    }

    var isPinned = false
    set(value) {
        field =value
        pinIndicator.visibility = if (isPinned) View.VISIBLE else View.GONE
    }

    init {
        itemView.setOnClickListener(this)
        itemView.isClickable = true
        pinIndicator.setImageBitmap(PiloterrIconsHelper.imageOfPinnedItem())
    }

    fun bind(item: ShopItem, canBuy: Boolean) {
        this.item = item
        buyButton.visibility = View.VISIBLE

        DataBindingUtils.loadImage(this.imageView, item.imageName?.replace("_locked", ""))

        itemDetailIndicator.text = null
        itemDetailIndicator.visibility = View.GONE

        val lockedReason = item.shortLockedReason(context)
        if (!item.locked || lockedReason == null) {
            priceLabel.text = item.value.toString()
            priceLabel.currency = item.currency
            if (item.currency == null) {
                buyButton.visibility = View.GONE
            }
            priceLabel.visibility = View.VISIBLE
            unlockLabel.visibility = View.GONE
            if (item.locked) {
                itemDetailIndicator.background = lockedDrawable
                itemDetailIndicator.visibility = View.VISIBLE
            }
        } else {
            unlockLabel.text = lockedReason
            priceLabel.visibility = View.GONE
            unlockLabel.visibility = View.VISIBLE
            itemDetailIndicator.background = lockedDrawable
            itemDetailIndicator.visibility = View.VISIBLE
        }

        if (item.isLimited) {
            itemDetailIndicator.background = limitedDrawable
            itemDetailIndicator.visibility = View.VISIBLE
        }

        priceLabel.isLocked = item.locked || !canBuy
    }

    override fun onClick(view: View) {
        val item = item
        if (item != null && item.isValid) {
            val dialog = PurchaseDialog(context, PiloterrBaseApplication.userComponent, item)
            dialog.shopIdentifier = shopIdentifier
            dialog.isPinned = isPinned
            dialog.purchaseCardAction = {
                purchaseCardAction?.invoke(it)
            }
            dialog.show()
        }
    }

    fun hidePinIndicator() {
        pinIndicator.visibility = View.GONE
    }
}
