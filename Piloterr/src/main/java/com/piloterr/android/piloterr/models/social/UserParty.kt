package com.piloterr.android.piloterr.models.social

import com.google.gson.annotations.SerializedName
import com.piloterr.android.piloterr.models.inventory.Quest

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UserParty : RealmObject() {
    @PrimaryKey
    var userId: String? = null
    @SerializedName("_id")
    var id: String = ""
    var quest: Quest? = null
    @SerializedName("order")
    var partyOrder: String? = null//Order to display ppl
    var orderAscending: String? = null//Order type

}
