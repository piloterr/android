package com.piloterr.android.piloterr.data.implementation

import com.piloterr.android.piloterr.data.ApiClient
import com.piloterr.android.piloterr.data.CustomizationRepository
import com.piloterr.android.piloterr.data.local.CustomizationLocalRepository
import com.piloterr.android.piloterr.models.inventory.Customization

import io.reactivex.Flowable
import io.realm.RealmResults

class CustomizationRepositoryImpl(localRepository: CustomizationLocalRepository, apiClient: ApiClient, userID: String) : BaseRepositoryImpl<CustomizationLocalRepository>(localRepository, apiClient, userID), CustomizationRepository {

    override fun getCustomizations(type: String, category: String?, onlyAvailable: Boolean): Flowable<RealmResults<Customization>> {
        return localRepository.getCustomizations(type, category, onlyAvailable)
    }
}
