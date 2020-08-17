package com.piloterr.android.piloterr.extensions

import android.content.Context
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.models.inventory.Animal

fun Animal.getTranslatedType(c: Context?): String {
    if (c == null) {
        return type
    }

    return when (type) {
        "drop"    -> c.getString(R.string.standard)
        "quest"   -> c.getString(R.string.quest)
        "wacky"   -> c.getString(R.string.wacky)
        "special" -> c.getString(R.string.special)
        "premium" -> c.getString(R.string.magic_potion)
        else      -> {
            type
        }
    }
}