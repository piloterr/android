package com.piloterr.android.piloterr.ui.views

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView


class PiloterrAutocompleteTextView(context: Context?, attrs: AttributeSet?) : AppCompatMultiAutoCompleteTextView(context, attrs) {

    init {
        val removed = this.inputType and (this.inputType xor InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE)
        this.inputType = removed
    }

}