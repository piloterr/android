package com.piloterr.android.piloterr.models.social

import com.piloterr.android.piloterr.models.Avatar
import com.piloterr.android.piloterr.models.AvatarPreferences
import com.piloterr.android.piloterr.models.user.Items
import com.piloterr.android.piloterr.models.user.Outfit
import com.piloterr.android.piloterr.models.user.Preferences
import com.piloterr.android.piloterr.models.user.Stats
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UserStyles : RealmObject(), Avatar {
    @PrimaryKey
    var id: String? = null
        set(value) {
            field = value
            stats?.userId = id
            preferences?.userId = id
            items?.userId = id
        }
    override fun getCurrentMount(): String? {
        return items?.currentMount
    }

    override fun getCurrentPet(): String? {
        return items?.currentPet
    }

    override fun getSleep(): Boolean {
        return false
    }

    override fun getStats(): Stats? {
        return stats
    }

    override fun getPreferences(): AvatarPreferences? {
        return preferences
    }

    override fun getGemCount(): Int {
        return 0
    }

    override fun getHourglassCount(): Int {
        return 0
    }

    override fun getCostume(): Outfit? {
        return items?.gear?.costume
    }

    override fun getEquipped(): Outfit? {
        return items?.gear?.equipped
    }

    override fun hasClass(): Boolean {
        return false
    }

    private var stats: Stats? = null
    private var preferences: Preferences? = null
    private var items: Items? = null
}
