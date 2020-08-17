package com.piloterr.android.piloterr;

import com.piloterr.android.piloterr.components.AppComponent;
import com.piloterr.android.piloterr.components.DaggerAppComponent;
import com.piloterr.android.piloterr.modules.AppModule;

public class TestApplication extends PiloterrBaseApplication {
    @Override
    protected AppComponent initDagger() {
            return DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .developerModule(new DebugDeveloperModule())
                    .repositoryModule(new TestRepositoryModule())
                    .build();
    }

    @Override
    protected void setupRealm() {
    }
}
