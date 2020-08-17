package com.piloterr.android.piloterr.proxy.implementation


import android.content.Context

import com.piloterr.android.piloterr.proxy.CrashlyticsProxy

class EmptyCrashlyticsProxy : CrashlyticsProxy {
    override fun init(context: Context) {
        //pass
    }

    override fun logException(e: Throwable) {
        //pass
    }

    override fun setString(key: String, value: String) {
        //pass
    }

    override fun setUserIdentifier(identifier: String) {
        //pass
    }

    override fun setUserName(name: String) {
        //pass
    }

    override fun fabricLogE(s1: String, s2: String, e: Exception) {
        //pass
    }

    override fun log(msg: String) {
        //pass
    }
}
