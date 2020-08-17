package com.piloterr.android.piloterr.ui.views.dialogs

import android.content.Context
import com.piloterr.android.piloterr.extensions.addCloseButton
import com.piloterr.android.piloterr.models.inventory.QuestContent
import com.piloterr.android.piloterr.ui.views.shops.PurchaseDialogQuestContent

class DetailDialog(context: Context) : PiloterrAlertDialog(context) {

    var quest: QuestContent? = null
    set(value) {
        field = value
        if (value == null) return

        val contentView = PurchaseDialogQuestContent(context)
        contentView.setQuestContent(value)
        contentView.setQuestContentItem(value)
        setAdditionalContentView(contentView)
    }

    init {
        addCloseButton()
    }
}