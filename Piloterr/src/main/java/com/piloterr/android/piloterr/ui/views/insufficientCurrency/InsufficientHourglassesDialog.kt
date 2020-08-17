package com.piloterr.android.piloterr.ui.views.insufficientCurrency

import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.extensions.addCloseButton
import com.piloterr.android.piloterr.helpers.MainNavigationController
import com.piloterr.android.piloterr.ui.views.PiloterrIconsHelper

class InsufficientHourglassesDialog(context: Context) : InsufficientCurrencyDialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageView.setImageBitmap(PiloterrIconsHelper.imageOfHourglassShop())
        textView.setText(R.string.insufficientHourglasses)

        addButton(R.string.get_hourglasses, true) { _, _ -> MainNavigationController.navigate(R.id.gemPurchaseActivity, bundleOf(Pair("openSubscription", true)))  }
        addCloseButton()
    }
}
