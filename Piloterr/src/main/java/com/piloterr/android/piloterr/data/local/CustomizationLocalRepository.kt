package com.piloterr.android.piloterr.data.local

import com.piloterr.android.piloterr.models.inventory.Customization

import io.reactivex.Flowable
import io.realm.RealmResults

interface CustomizationLocalRepository : ContentLocalRepository {
    fun getCustomizations(type: String, category: String?, onlyAvailable: Boolean): Flowable<RealmResults<Customization>>
}
