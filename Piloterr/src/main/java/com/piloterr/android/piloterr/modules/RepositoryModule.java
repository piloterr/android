package com.piloterr.android.piloterr.modules;

import com.piloterr.android.piloterr.data.ApiClient;
import com.piloterr.android.piloterr.data.ContentRepository;
import com.piloterr.android.piloterr.data.implementation.ContentRepositoryImpl;
import com.piloterr.android.piloterr.data.local.ContentLocalRepository;
import com.piloterr.android.piloterr.data.local.implementation.RealmContentLocalRepository;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class RepositoryModule {
    @Provides
    public Realm providesRealm() {
        return Realm.getDefaultInstance();
    }

    @Provides
    public ContentLocalRepository providesContentLocalRepository(Realm realm) { return new RealmContentLocalRepository(realm); }

    @Provides
    public ContentRepository providesContentRepository(ContentLocalRepository contentLocalRepository, ApiClient apiClient) { return new ContentRepositoryImpl<ContentLocalRepository>(contentLocalRepository, apiClient) {}; }
}
