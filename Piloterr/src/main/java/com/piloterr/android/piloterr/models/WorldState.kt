package com.piloterr.android.piloterr.models

import com.facebook.internal.Mutable
import com.piloterr.android.piloterr.models.inventory.QuestProgress
import com.piloterr.android.piloterr.models.inventory.QuestRageStrike

class WorldState {

    var worldBossKey: String = ""
    var worldBossActive: Boolean = false
    var progress: QuestProgress? = null
    var rageStrikes: MutableList<QuestRageStrike>? = null

}