package com.piloterr.android.piloterr.data.local.implementation

import com.piloterr.android.piloterr.data.local.CustomizationLocalRepository
import com.piloterr.android.piloterr.models.inventory.Customization
import io.reactivex.Flowable
import io.realm.Realm
import io.realm.RealmResults
import java.util.*


class RealmCustomizationLocalRepository(realm: Realm) : RealmContentLocalRepository(realm), CustomizationLocalRepository {

    override fun getCustomizations(type: String, category: String?, onlyAvailable: Boolean): Flowable<RealmResults<Customization>> {
        var query = realm.where(Customization::class.java)
                .equalTo("type", type)
                .equalTo("category", category)
        if (onlyAvailable) {
            val today = Date()
            query = query
                    .beginGroup()
                    .beginGroup()
                    .lessThanOrEqualTo("availableFrom", today)
                    .greaterThanOrEqualTo("availableUntil", today)
                    .endGroup()
                    .or()
                    .beginGroup()
                    .isNull("availableFrom")
                    .isNull("availableUntil")
                    .endGroup()
                    .endGroup()
        }
        return query
                .sort("customizationSetName")
                .findAll()
                .asFlowable()
                .filter { it.isLoaded }
    }
}
