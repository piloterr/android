package com.piloterr.android.piloterr.ui.fragments.inventory.shops

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class MarketFragment: ShopsFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lockTab = 0
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}