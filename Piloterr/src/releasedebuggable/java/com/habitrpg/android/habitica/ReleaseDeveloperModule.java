package com.piloterr.android.piloterr;


import com.piloterr.android.piloterr.modules.DeveloperModule;
import com.piloterr.android.piloterr.proxy.CrashlyticsProxyImpl;
import com.piloterr.android.piloterr.proxy.ifce.CrashlyticsProxy;

//change debug proxy here by override methods
public class ReleaseDeveloperModule extends DeveloperModule {
    @Override protected CrashlyticsProxy provideCrashlyticsProxy() {
        return new CrashlyticsProxyImpl();
    }
}
