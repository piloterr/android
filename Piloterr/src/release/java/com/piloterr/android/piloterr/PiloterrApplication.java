package com.piloterr.android.piloterr;

import com.piloterr.android.piloterr.components.AppComponent;
import com.piloterr.android.piloterr.components.DaggerAppComponent;
import com.piloterr.android.piloterr.modules.AppModule;

public class PiloterrApplication extends PiloterrBaseApplication {
    @Override
    protected AppComponent initDagger() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .developerModule(new ReleaseDeveloperModule())
                .build();
    }

}
