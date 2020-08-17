package com.piloterr.android.piloterr;


import com.piloterr.android.piloterr.modules.DeveloperModule;
import com.piloterr.android.piloterr.proxy.CrashlyticsProxyImpl;
import com.piloterr.android.piloterr.proxy.CrashlyticsProxy;

import android.content.Context;

//change debug proxy here by override methods
public class ReleaseDeveloperModule extends DeveloperModule {
    @Override protected CrashlyticsProxy provideCrashlyticsProxy(Context context) {
        CrashlyticsProxy crashlyticsProxy = new CrashlyticsProxyImpl();
        crashlyticsProxy.init(context);
        return crashlyticsProxy;
    }
}
