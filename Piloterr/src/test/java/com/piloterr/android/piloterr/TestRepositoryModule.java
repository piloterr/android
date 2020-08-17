package com.piloterr.android.piloterr;

import com.piloterr.android.piloterr.modules.RepositoryModule;

import org.mockito.Mockito;

import dagger.Provides;
import io.realm.Realm;

class TestRepositoryModule extends RepositoryModule {

    @Provides
    @Override
    public Realm providesRealm() {
        return Mockito.mock(Realm.class);
    }
}
