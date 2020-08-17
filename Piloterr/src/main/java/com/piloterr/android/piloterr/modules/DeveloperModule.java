package com.piloterr.android.piloterr.modules;


import android.content.Context;

import com.piloterr.android.piloterr.proxy.CrashlyticsProxy;
import com.piloterr.android.piloterr.proxy.implementation.EmptyCrashlyticsProxy;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

//provide proxy class for libraries(to avoid 65k limit)
@Module
public class DeveloperModule {
    @Provides
    @Singleton
    protected CrashlyticsProxy provideCrashlyticsProxy(Context context) {
        return new EmptyCrashlyticsProxy();
    }

}
