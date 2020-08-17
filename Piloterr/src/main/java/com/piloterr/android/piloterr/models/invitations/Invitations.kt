package com.piloterr.android.piloterr.models.invitations

import com.piloterr.android.piloterr.models.user.User

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Invitations : RealmObject() {

    @PrimaryKey
    var userId: String? = null

    internal var user: User? = null

    var party: PartyInvite? = null
    var parties: RealmList<PartyInvite>? = null
    var guilds: RealmList<GuildInvite>? = null

    fun removeInvitation(groupID: String) {
        if (party?.id == groupID) {
            party = null
        }

        guilds?.removeAll {
            it.id == groupID
        }

        parties?.removeAll {
            it.id == groupID
        }
    }
}
