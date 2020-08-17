package com.piloterr.android.piloterr.ui.viewHolders

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.extensions.inflate
import com.piloterr.android.piloterr.ui.activities.GiftOneGetOneInfoActivity
import kotlinx.android.synthetic.main.promo_subscription_buy_gems.view.*

class GiftOneGetOnePromoMenuView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
        inflate(R.layout.promo_gift_one_get_one, true)
        setBackgroundColor(ContextCompat.getColor(context, R.color.teal_50))
        clipToPadding = false
        clipChildren = false
        clipToOutline = false
        button.setOnClickListener {
            val intent = Intent(context, GiftOneGetOneInfoActivity::class.java)
            context.startActivity(intent)
        }
    }


}
