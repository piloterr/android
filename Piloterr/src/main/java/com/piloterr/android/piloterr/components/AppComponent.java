package com.piloterr.android.piloterr.components;

import com.piloterr.android.piloterr.PiloterrBaseApplication;
import com.piloterr.android.piloterr.modules.ApiModule;
import com.piloterr.android.piloterr.modules.AppModule;
import com.piloterr.android.piloterr.modules.DeveloperModule;
import com.piloterr.android.piloterr.modules.RepositoryModule;
import com.piloterr.android.piloterr.modules.UserRepositoryModule;
import com.piloterr.android.piloterr.modules.UserModule;

import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {DeveloperModule.class, AppModule.class, ApiModule.class, RepositoryModule.class})
public interface AppComponent {

    UserComponent plus(UserModule userModule, UserRepositoryModule userRepositoryModule);

    void inject(@NotNull PiloterrBaseApplication piloterrBaseApplication);
}
