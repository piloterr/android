package com.piloterr.android.piloterr.ui.views.tasks.form

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.extensions.asDrawable
import com.piloterr.android.piloterr.ui.helpers.bindView
import com.piloterr.android.piloterr.ui.views.PiloterrIconsHelper

class HabitScoringButtonsView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val positiveView: ViewGroup by bindView(R.id.positive_view)
    private val negativeView: ViewGroup by bindView(R.id.negative_view)
    private val positiveImageView: ImageView by bindView(R.id.positive_image_view)
    private val negativeImageView: ImageView by bindView(R.id.negative_image_view)
    private val positiveTextView: TextView by bindView(R.id.positive_text_view)
    private val negativeTextView: TextView by bindView(R.id.negative_text_view)


    var tintColor: Int = ContextCompat.getColor(context, R.color.brand_300)
    var textTintColor: Int? = null

    var isPositive = true
        set(value) {
            field = value
            positiveImageView.setImageDrawable(PiloterrIconsHelper.imageOfHabitControlPlus(tintColor, value).asDrawable(resources))
            if (value) {
                positiveTextView.setTextColor(textTintColor ?: tintColor)
                positiveView.contentDescription = toContentDescription(R.string.positive_habit_form, R.string.on)
                positiveTextView.typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
            } else {
                positiveTextView.setTextColor(ContextCompat.getColor(context, R.color.gray_100))
                positiveView.contentDescription = toContentDescription(R.string.positive_habit_form, R.string.off)
                positiveTextView.typeface = Typeface.create("sans-serif", Typeface.NORMAL)
            }
        }

    var isNegative = true
        set(value) {
            field = value
            negativeImageView.setImageDrawable(PiloterrIconsHelper.imageOfHabitControlMinus(tintColor, value).asDrawable(resources))
            if (value) {
                negativeTextView.setTextColor(textTintColor ?: tintColor)
                negativeView.contentDescription = toContentDescription(R.string.negative_habit_form, R.string.on)
                negativeTextView.typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
            } else {
                negativeTextView.setTextColor(ContextCompat.getColor(context, R.color.gray_100))
                negativeView.contentDescription = toContentDescription(R.string.negative_habit_form, R.string.off)
                negativeTextView.typeface = Typeface.create("sans-serif", Typeface.NORMAL)
            }
        }

    private fun toContentDescription(descriptionStringId: Int, statusStringId: Int): String {
        return context.getString(descriptionStringId) + ", " + context.getString(statusStringId)
    }

    init {
        View.inflate(context, R.layout.task_form_habit_scoring, this)
        gravity = Gravity.CENTER

        positiveView.setOnClickListener {
            isPositive = !isPositive
            sendAccessibilityEvent(AccessibilityEvent.CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION)
        }
        negativeView.setOnClickListener {
            isNegative = !isNegative
            sendAccessibilityEvent(AccessibilityEvent.CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION)
        }

        isPositive = true
        isNegative = true
    }
}
