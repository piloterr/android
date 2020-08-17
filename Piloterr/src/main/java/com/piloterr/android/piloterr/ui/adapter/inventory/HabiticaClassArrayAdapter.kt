package com.piloterr.android.piloterr.ui.adapter.inventory

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.extensions.inflate
import com.piloterr.android.piloterr.models.user.Stats
import com.piloterr.android.piloterr.ui.views.PiloterrIconsHelper


class PiloterrClassArrayAdapter(context: Context, resource: Int, objects: List<CharSequence>) : ArrayAdapter<CharSequence>(context, resource, R.id.textView, objects) {

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View =
            createView(position, convertView ?: parent?.inflate(R.layout.class_spinner_dropdown_item, false))

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View =
            createView(position, convertView ?: parent.inflate(R.layout.class_spinner_dropdown_item_selected, false))

    private fun createView(position: Int, row: View?): View {
        val textView: TextView? = row?.findViewById(R.id.textView)
        val imageView: ImageView? = row?.findViewById(R.id.classIconView)

        when (getItem(position)) {
            Stats.WARRIOR -> {
                textView?.text = context.getString(R.string.warrior)
                textView?.setTextColor(ContextCompat.getColor(context, R.color.red_10))
                imageView?.setImageBitmap(PiloterrIconsHelper.imageOfWarriorLightBg())
            }
            Stats.MAGE -> {
                textView?.text = context.getString(R.string.mage)
                textView?.setTextColor(ContextCompat.getColor(context, R.color.blue_10))
                imageView?.setImageBitmap(PiloterrIconsHelper.imageOfMageLightBg())
            }
            Stats.HEALER -> {
                textView?.text = context.getString(R.string.healer)
                textView?.setTextColor(ContextCompat.getColor(context, R.color.yellow_10))
                imageView?.setImageBitmap(PiloterrIconsHelper.imageOfHealerLightBg())
            }
            Stats.ROGUE -> {
                textView?.text = context.getString(R.string.rogue)
                textView?.setTextColor(ContextCompat.getColor(context, R.color.brand_300))
                imageView?.setImageBitmap(PiloterrIconsHelper.imageOfRogueLightBg())
            }
            else -> {
                textView?.text = context.getString(R.string.classless)
                textView?.setTextColor(ContextCompat.getColor(context, R.color.textColorLight))
                imageView?.setImageBitmap(null)
            }
        }
        return row ?: View(context)
    }
}