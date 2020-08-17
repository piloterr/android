package com.piloterr.android.piloterr.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.databinding.PurchaseGemViewBinding
import com.piloterr.android.piloterr.extensions.layoutInflater


class GemPurchaseOptionsView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var binding: PurchaseGemViewBinding = PurchaseGemViewBinding.inflate(context.layoutInflater, this, true)
    var sku: String? = null

    init {

        val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.GemPurchaseOptionsView,
                0, 0)

        binding.gemAmount.text = a.getText(R.styleable.GemPurchaseOptionsView_gemAmount)

        val iconRes = a.getDrawable(R.styleable.GemPurchaseOptionsView_gemDrawable)
        if (iconRes != null) {
            binding.gemImage.setImageDrawable(iconRes)
        }
    }

    fun setOnPurchaseClickListener(listener: OnClickListener) {
        binding.purchaseButton.setOnClickListener(listener)
    }

    fun setPurchaseButtonText(price: String) {
        binding.purchaseButton.text = price
    }
}
