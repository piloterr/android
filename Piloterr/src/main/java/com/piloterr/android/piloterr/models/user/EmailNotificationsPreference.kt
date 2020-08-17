package com.piloterr.android.piloterr.models.user

import io.realm.RealmObject

open class EmailNotificationsPreference : RealmObject() {
    var unsubscribeFromAll: Boolean = false
    var invitedParty: Boolean = false
    var invitedQuest: Boolean = false
    var majorUpdates: Boolean = false
    var wonChallenge: Boolean = false
    var invitedGuild: Boolean = false
    var newPM: Boolean = false
    var questStarted: Boolean = false
    var giftedGems: Boolean = false
    var giftedSubscription: Boolean = false
    var subscriptionReminders: Boolean = false
    var onboarding: Boolean = false
    var kickedGroup: Boolean = false
}
