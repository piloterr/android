package com.piloterr.android.piloterr.models.invitations

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class GuildInvite : RealmObject(), GenericInvitation {

    @PrimaryKey
    override var id: String? = null
    override var inviter: String? = null
    override var name: String? = null
    var publicGuild: Boolean? = null
}
