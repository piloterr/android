package com.piloterr.android.piloterr.helpers

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import java.lang.ref.WeakReference
import java.util.*
import kotlin.math.abs

object MainNavigationController {
    var lastNavigation: Date? = null

    var navController: WeakReference<NavController>? = null

    fun setup(navController: NavController) {
        this.navController = WeakReference(navController)
    }

    fun navigate(transactionId: Int, args: Bundle? = null) {
        if (abs((lastNavigation?.time ?: 0) - Date().time) > 500) {
            lastNavigation = Date()
            try {
                navController?.get()?.navigate(transactionId, args)
            } catch (_: IllegalArgumentException) {}
        }
    }

    fun navigate(directions: NavDirections) {
        if (abs((lastNavigation?.time ?: 0) - Date().time) > 500) {
            lastNavigation = Date()
            try {
            navController?.get()?.navigate(directions)
            } catch (_: IllegalArgumentException) {}
        }
    }
}