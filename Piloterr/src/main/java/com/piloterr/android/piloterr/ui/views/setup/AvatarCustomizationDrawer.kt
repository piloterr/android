package com.piloterr.android.piloterr.ui.views.setup


import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.extensions.inflate

class AvatarCustomizationDrawer(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    init {
        inflate(R.layout.avatar_setup_drawer, true)
    }
}
