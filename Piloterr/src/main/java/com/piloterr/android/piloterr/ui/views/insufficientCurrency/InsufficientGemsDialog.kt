package com.piloterr.android.piloterr.ui.views.insufficientCurrency

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.piloterr.android.piloterr.PiloterrBaseApplication
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.events.ConsumablePurchasedEvent
import com.piloterr.android.piloterr.extensions.addCloseButton
import com.piloterr.android.piloterr.helpers.AppConfigManager
import com.piloterr.android.piloterr.helpers.MainNavigationController
import com.piloterr.android.piloterr.helpers.PurchaseHandler
import com.piloterr.android.piloterr.helpers.PurchaseTypes
import com.piloterr.android.piloterr.proxy.CrashlyticsProxy
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

/**
 * Created by phillip on 27.09.17.
 */

class InsufficientGemsDialog(context: Context, var gemPrice: Int) : InsufficientCurrencyDialog(context) {

    private var purchaseButton: Button? = null
    @Inject
    lateinit var configManager: AppConfigManager
    @Inject
    lateinit var crashlyticsProxy: CrashlyticsProxy

    private var purchaseHandler: PurchaseHandler? = null

    override fun getLayoutID(): Int {
        return R.layout.dialog_insufficient_gems
    }

    var sku: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        PiloterrBaseApplication.userComponent?.inject(this)
        super.onCreate(savedInstanceState)
        imageView.setImageResource(R.drawable.gems_84)
        textView.setText(R.string.insufficientGems)
        addCloseButton()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        getActivity()?.let {
            if (configManager.insufficientGemPurchase()) {
                purchaseButton = contentView.findViewById(R.id.purchase_button)
                purchaseHandler = PurchaseHandler(it, crashlyticsProxy)
                purchaseHandler?.startListening()
                purchaseHandler?.whenCheckoutReady = {
                    sku = if (configManager.insufficientGemPurchaseAdjust()) {
                        if (gemPrice > 4) {
                            PurchaseTypes.Purchase21Gems
                        } else {
                            PurchaseTypes.Purchase4Gems
                        }
                    } else {
                        PurchaseTypes.Purchase4Gems
                    }
                    purchaseHandler?.getInAppPurchaseSKU(PurchaseTypes.Purchase4Gems) { sku ->
                        val purchaseTextView = contentView.findViewById<TextView>(R.id.purchase_textview)
                        purchaseTextView.text = sku.displayTitle
                        purchaseButton?.text = sku.price
                    }
                }

                purchaseButton?.setOnClickListener {
                    FirebaseAnalytics.getInstance(context).logEvent("purchased_gems_from_insufficient", bundleOf(Pair("gemPrice", gemPrice), Pair("sku", "")))
                    purchaseHandler?.purchaseGems(PurchaseTypes.Purchase4Gems)
                }
                addButton(R.string.see_other_options, false) { _, _ -> MainNavigationController.navigate(R.id.gemPurchaseActivity, bundleOf(Pair("openSubscription", false))) }
            } else {
                contentView.findViewById<LinearLayout>(R.id.purchase_wrapper).visibility = View.GONE
                addButton(R.string.purchase_gems, false) { _, _ -> MainNavigationController.navigate(R.id.gemPurchaseActivity, bundleOf(Pair("openSubscription", false))) }
            }
        }
        EventBus.getDefault().register(this)
    }

    override fun onDetachedFromWindow() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        super.onDetachedFromWindow()
    }

    @Subscribe
    fun onConsumablePurchased(event: ConsumablePurchasedEvent) {
        purchaseHandler?.consumePurchase(event.purchase)
        dismiss()
    }
}
