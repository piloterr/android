package com.piloterr.android.piloterr.extensions

import android.content.DialogInterface
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.ui.views.dialogs.PiloterrAlertDialog

fun PiloterrAlertDialog.addOkButton(isPrimary: Boolean = true, listener: ((PiloterrAlertDialog, Int) -> Unit)? = null) {
    this.addButton(R.string.ok, isPrimary, false, listener)
}

fun PiloterrAlertDialog.addCloseButton(isPrimary: Boolean = false, listener: ((DialogInterface, Int) -> Unit)? = null) {
    this.addButton(R.string.close, isPrimary, false, listener)
}

fun PiloterrAlertDialog.addCancelButton(isPrimary: Boolean = false, listener: ((DialogInterface, Int) -> Unit)? = null) {
    this.addButton(R.string.cancel, isPrimary, false, listener)
}