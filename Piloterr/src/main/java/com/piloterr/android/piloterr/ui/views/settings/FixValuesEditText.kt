package com.piloterr.android.piloterr.ui.views.settings

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.databinding.FixvaluesEdittextBinding
import com.piloterr.android.piloterr.extensions.layoutInflater
import com.piloterr.android.piloterr.ui.views.PiloterrIconsHelper

class FixValuesEditText(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var binding: FixvaluesEdittextBinding
    var text: String
    get() = binding.editText.text.toString()
    set(value) {
        binding.editText.setText(value)
        binding.editText.hint = value
    }

    @ColorRes
    var iconBackgroundColor: Int = 0
    set(value) {
        field = value
        val backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.layout_rounded_bg)
        backgroundDrawable?.setColorFilter(field, PorterDuff.Mode.MULTIPLY)
        backgroundDrawable?.alpha = 50
        binding.iconBackgroundView.background = backgroundDrawable
    }

    init {
        val attributes = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.FixValuesEditText,
                0, 0)

        val view = context.layoutInflater.inflate(R.layout.fixvalues_edittext, this, true)
        binding = FixvaluesEdittextBinding.bind(view)

        binding.editText.hint = attributes.getString(R.styleable.FixValuesEditText_title)
        binding.editTextWrapper.hint = binding.editText.hint
        binding.editTextWrapper.setHintTextAppearance(attributes.getResourceId(R.styleable.FixValuesEditText_hintStyle, R.style.PurpleTextLabel))
        iconBackgroundColor = attributes.getColor(R.styleable.FixValuesEditText_iconBgColor, 0)

        when (attributes.getString(R.styleable.FixValuesEditText_fixIconName)) {
            "health" -> binding.iconView.setImageBitmap(PiloterrIconsHelper.imageOfHeartLightBg())
            "experience" -> binding.iconView.setImageBitmap(PiloterrIconsHelper.imageOfExperience())
            "mana" -> binding.iconView.setImageBitmap(PiloterrIconsHelper.imageOfMagic())
            "gold" -> binding.iconView.setImageBitmap(PiloterrIconsHelper.imageOfGold())
            "level" -> binding.iconView.setImageBitmap(PiloterrIconsHelper.imageOfRogueLightBg())
            "streak" -> binding.iconView.setImageResource(R.drawable.achievement_thermometer)
        }
    }

    fun setIconBitmap(icon: Bitmap) {
        binding.iconView.setImageBitmap(icon)
    }
}
