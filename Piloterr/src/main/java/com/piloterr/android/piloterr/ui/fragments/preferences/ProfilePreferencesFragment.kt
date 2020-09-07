package com.piloterr.android.piloterr.ui.fragments.preferences

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import com.piloterr.android.piloterr.PiloterrBaseApplication
import com.piloterr.android.piloterr.helpers.RxErrorHandler
import com.piloterr.android.piloterr.models.user.User
import io.reactivex.Flowable
import io.reactivex.functions.Consumer

class ProfilePreferencesFragment: BasePreferencesFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    override var user: User? = null
    set(value) {
        field = value
        updateUserFields()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        PiloterrBaseApplication.userComponent?.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)

    }

    override fun onPause() {
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onPause()
    }

    private fun updateUserFields() {
        configurePreference(findPreference("display_name"), user?.profile?.name)
        configurePreference(findPreference("photo_url"), user?.profile?.imageUrl)
        configurePreference(findPreference("about"), user?.profile?.blurb)
    }

    private fun configurePreference(preference: Preference?, value: String?) {
        val editPreference = preference as? EditTextPreference
        editPreference?.text = value
        preference?.summary = value
    }

    override fun setupPreferences() {
        updateUserFields()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val profileCategory = findPreference("profile") as? PreferenceCategory
        configurePreference(profileCategory?.findPreference(key), sharedPreferences?.getString(key, ""))
        if (sharedPreferences != null) {
            val newValue = sharedPreferences.getString(key, "") ?: return
            val observable: Flowable<User>? = when (key) {
                "display_name" -> {
                    if (newValue != user?.profile?.name) {
                        userRepository.updateUser(user, "profile.name", newValue)
                    } else {
                        null
                    }
                }
                "photo_url" -> {
                    if (newValue != user?.profile?.imageUrl) {
                        userRepository.updateUser(user, "profile.imageUrl", newValue)
                    } else {
                        null
                    }
                }
                "about" -> {
                    if (newValue != user?.profile?.blurb) {
                        userRepository.updateUser(user, "profile.blurb", newValue)
                    } else {
                        null
                    }
                }
                else -> null
            }
            observable?.subscribe(Consumer {}, RxErrorHandler.handleEmptyError())?.let { compositeSubscription.add(it) }
        }
    }
}