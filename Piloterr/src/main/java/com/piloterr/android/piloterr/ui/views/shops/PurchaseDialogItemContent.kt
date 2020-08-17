package com.piloterr.android.piloterr.ui.views.shops

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.extensions.asDrawable
import com.piloterr.android.piloterr.ui.helpers.bindView
import com.piloterr.android.piloterr.models.shops.ShopItem
import com.piloterr.android.piloterr.ui.views.PiloterrIconsHelper
import com.piloterr.android.piloterr.ui.views.tasks.form.StepperValueFormView

class PurchaseDialogItemContent : PurchaseDialogContent {

    internal val notesTextView: TextView by bindView(R.id.notesTextView)
    val stepperView: StepperValueFormView by bindView(R.id.stepper_view)

    override val viewId: Int
        get() = R.layout.dialog_purchase_content_item

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun setItem(item: ShopItem) {
        super.setItem(item)
        notesTextView.text = item.notes
        stepperView.iconDrawable = null
    }
}
