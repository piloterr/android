package com.piloterr.android.piloterr.ui.views.adventureGuide

import android.content.Context
import android.os.Build
import android.text.Html
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.databinding.AdventureGuideMenuBannerBinding
import com.piloterr.android.piloterr.databinding.EquipmentOverviewItemBinding
import com.piloterr.android.piloterr.extensions.layoutInflater
import com.piloterr.android.piloterr.models.user.User

class AdventureGuideMenuBanner @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var binding = AdventureGuideMenuBannerBinding.inflate(context.layoutInflater, this)

    init {
        background = ContextCompat.getDrawable(context, R.drawable.adventure_guide_menu_bg)
        val descriptionText = context.getString(R.string.complete_for_gold)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.descriptionView.setText(Html.fromHtml(descriptionText,  Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE)
        } else {
            binding.descriptionView.setText(Html.fromHtml(descriptionText), TextView.BufferType.SPANNABLE)
        }
    }

    fun updateData(user: User) {
        val achievements = user.onboardingAchievements
        val completed = achievements.count { it.earned }
        binding.progressBar.max = achievements.size
        binding.progressBar.progress = completed
        binding.countView.text = "${completed} / ${achievements.size}"
    }
}