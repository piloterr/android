package com.piloterr.android.piloterr.ui.views.equipment

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.databinding.EquipmentOverviewItemBinding
import com.piloterr.android.piloterr.extensions.layoutInflater
import com.piloterr.android.piloterr.ui.helpers.DataBindingUtils
import com.piloterr.android.piloterr.ui.views.PiloterrIconsHelper

class EquipmentOverviewItem @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var binding: EquipmentOverviewItemBinding = EquipmentOverviewItemBinding.inflate(context.layoutInflater, this)

    init {
        if (attrs != null) {
            val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.EquipmentOverviewItem)
            binding.titleView.text = styledAttrs.getString(R.styleable.EquipmentOverviewItem_title)
            styledAttrs.recycle()
        }
        orientation = VERTICAL
    }

    var identifier: String = ""

    fun set(key: String, isTwoHanded: Boolean = false, isDisabledFromTwoHand: Boolean = false) {
        identifier = key
        binding.twoHandedIndicator.setImageDrawable(null)
        if (key.isNotEmpty() && !key.endsWith("base_0")) {
            DataBindingUtils.loadImage(binding.iconView, "shop_$key")
            binding.localIconView.visibility = View.GONE
            binding.iconView.visibility = View.VISIBLE
            binding.iconWrapper.background = ContextCompat.getDrawable(context, R.drawable.layout_rounded_bg_white)
            if (isTwoHanded) {
                binding.twoHandedIndicator.setImageDrawable(BitmapDrawable(context.resources, PiloterrIconsHelper.imageOfTwoHandedIcon()))
            }
        } else {
            binding.localIconView.visibility = View.VISIBLE
            binding.iconView.visibility = View.GONE
            if (isDisabledFromTwoHand) {
                binding.iconWrapper.background = ContextCompat.getDrawable(context, R.drawable.layout_rounded_bg_white)
                binding.localIconView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.equipment_two_handed))
            } else {
                binding.iconWrapper.background = ContextCompat.getDrawable(context, R.drawable.layout_rounded_bg_gray_10)
                binding.localIconView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.equipment_nothing_equipped))
            }
        }
    }
}