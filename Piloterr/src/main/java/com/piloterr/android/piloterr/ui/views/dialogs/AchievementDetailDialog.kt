package com.piloterr.android.piloterr.ui.views.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.extensions.addCloseButton
import com.piloterr.android.piloterr.models.Achievement
import com.piloterr.android.piloterr.ui.helpers.DataBindingUtils

class AchievementDetailDialog(val achievement: Achievement, context: Context): PiloterrAlertDialog(context) {

    private var iconView: SimpleDraweeView?
    private var descriptionView: TextView?

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as? LayoutInflater
        val view = inflater?.inflate(R.layout.dialog_achievement_detail, null)
        iconView = view?.findViewById(R.id.icon_view)
        descriptionView = view?.findViewById(R.id.description_view)
        setAdditionalContentView(view)
        setTitle(achievement.title)
        descriptionView?.text = achievement.text
        val iconName = if (achievement.earned) {
            achievement.icon + "2x"
        } else {
            "achievement-unearned2x"
        }
        DataBindingUtils.loadImage(iconView, iconName)
        addCloseButton(true)
    }
}