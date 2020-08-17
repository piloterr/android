package com.piloterr.android.piloterr.models.shops

import android.content.Context
import com.piloterr.android.piloterr.R

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ShopItemUnlockCondition : RealmObject() {

    @PrimaryKey
    var questKey: String? = null
    private var condition: String? = null
    var incentiveThreshold: Int? = null

    fun readableUnlockCondition(context: Context): String = when (this.condition) {
        "party invite" -> context.getString(R.string.party_invite)
        "login reward" -> if (incentiveThreshold != null) context.getString(R.string.login_incentive_count, incentiveThreshold) else  context.getString(R.string.login_incentive)
        "create account" -> context.getString(R.string.create_account)
        else -> ""
    }

    fun shortReadableUnlockCondition(context: Context): String = when (this.condition) {
        "party invite" -> context.getString(R.string.party_invite_short)
        "login reward" -> if (incentiveThreshold != null) context.getString(R.string.login_incentive_short_count, incentiveThreshold) else  context.getString(R.string.login_incentive_short)
        "create account" -> context.getString(R.string.create_account_short)
        else -> ""
    }
}
