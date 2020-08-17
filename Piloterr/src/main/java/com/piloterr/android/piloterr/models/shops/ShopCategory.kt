package com.piloterr.android.piloterr.models.shops

class ShopCategory {

    var identifier: String = ""
    var text: String = ""
    var notes: String = ""
    var path: String = ""
    var purchaseAll: Boolean? = null
    var pinType: String = ""

    var items: MutableList<ShopItem> = ArrayList()
}
