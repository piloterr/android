package com.piloterr.android.piloterr.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.databinding.SupportCollapsibleSectionBinding
import com.piloterr.android.piloterr.extensions.layoutInflater
import com.piloterr.android.piloterr.ui.helpers.MarkdownParser

class SupportCollapsibleSection : LinearLayout {
    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val binding = SupportCollapsibleSectionBinding.inflate(context.layoutInflater, this)
        val a = context.obtainStyledAttributes(
                attrs, R.styleable.SupportCollapsibleSection, defStyle, 0)

        orientation = VERTICAL

        binding.titleView.text = a.getString(R.styleable.SupportCollapsibleSection_title)
        binding.subtitleView.text = a.getString(R.styleable.SupportCollapsibleSection_subtitle)
        binding.descriptionView.text = MarkdownParser.parseMarkdown(a.getString(R.styleable.SupportCollapsibleSection_description))
        binding.titleView.setTextColor(a.getColor(R.styleable.SupportCollapsibleSection_titleColor, ContextCompat.getColor(context, R.color.gray_50)))

        background = context.getDrawable(R.drawable.layout_rounded_bg_gray_700)

        a.recycle()

        setOnClickListener {
            binding.descriptionView.visibility = if (binding.descriptionView.visibility == View.VISIBLE){
                binding.caretView.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp)
                View.GONE
            } else{
                binding.caretView.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp)
                View.VISIBLE
            }
        }
    }
}
