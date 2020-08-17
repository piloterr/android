package com.piloterr.android.piloterr.models.social

import com.google.gson.annotations.SerializedName
import com.piloterr.android.piloterr.models.user.Authentication
import com.piloterr.android.piloterr.models.user.ContributorInfo
import com.piloterr.android.piloterr.models.user.Profile
import io.realm.annotations.PrimaryKey

class FindUsernameResult {

    @SerializedName("_id")
    var id: String? = null

    var contributor: ContributorInfo? = null
    var authentication: Authentication? = null
    var profile: Profile? = null

    val username: String?
        get() = authentication?.localAuthentication?.username
    val formattedUsername: String?
        get() = if (username != null) "@$username" else null

    override fun toString(): String {
        return "@${authentication?.localAuthentication?.username ?: ""}"
    }
}
