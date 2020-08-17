package com.piloterr.android.piloterr.modules;


import android.content.Context;

import com.piloterr.android.piloterr.data.ApiClient;
import com.piloterr.android.piloterr.data.ChallengeRepository;
import com.piloterr.android.piloterr.data.CustomizationRepository;
import com.piloterr.android.piloterr.data.FAQRepository;
import com.piloterr.android.piloterr.data.InventoryRepository;
import com.piloterr.android.piloterr.data.SetupCustomizationRepository;
import com.piloterr.android.piloterr.data.SocialRepository;
import com.piloterr.android.piloterr.data.TagRepository;
import com.piloterr.android.piloterr.data.TaskRepository;
import com.piloterr.android.piloterr.data.TutorialRepository;
import com.piloterr.android.piloterr.data.UserRepository;
import com.piloterr.android.piloterr.data.implementation.ChallengeRepositoryImpl;
import com.piloterr.android.piloterr.data.implementation.CustomizationRepositoryImpl;
import com.piloterr.android.piloterr.data.implementation.FAQRepositoryImpl;
import com.piloterr.android.piloterr.data.implementation.InventoryRepositoryImpl;
import com.piloterr.android.piloterr.data.implementation.SetupCustomizationRepositoryImpl;
import com.piloterr.android.piloterr.data.implementation.SocialRepositoryImpl;
import com.piloterr.android.piloterr.data.implementation.TagRepositoryImpl;
import com.piloterr.android.piloterr.data.implementation.TaskRepositoryImpl;
import com.piloterr.android.piloterr.data.implementation.TutorialRepositoryImpl;
import com.piloterr.android.piloterr.data.implementation.UserRepositoryImpl;
import com.piloterr.android.piloterr.data.local.ChallengeLocalRepository;
import com.piloterr.android.piloterr.data.local.CustomizationLocalRepository;
import com.piloterr.android.piloterr.data.local.FAQLocalRepository;
import com.piloterr.android.piloterr.data.local.InventoryLocalRepository;
import com.piloterr.android.piloterr.data.local.SocialLocalRepository;
import com.piloterr.android.piloterr.data.local.TagLocalRepository;
import com.piloterr.android.piloterr.data.local.TaskLocalRepository;
import com.piloterr.android.piloterr.data.local.TutorialLocalRepository;
import com.piloterr.android.piloterr.data.local.UserLocalRepository;
import com.piloterr.android.piloterr.data.local.implementation.RealmChallengeLocalRepository;
import com.piloterr.android.piloterr.data.local.implementation.RealmCustomizationLocalRepository;
import com.piloterr.android.piloterr.data.local.implementation.RealmFAQLocalRepository;
import com.piloterr.android.piloterr.data.local.implementation.RealmInventoryLocalRepository;
import com.piloterr.android.piloterr.data.local.implementation.RealmSocialLocalRepository;
import com.piloterr.android.piloterr.data.local.implementation.RealmTagLocalRepository;
import com.piloterr.android.piloterr.data.local.implementation.RealmTaskLocalRepository;
import com.piloterr.android.piloterr.data.local.implementation.RealmTutorialLocalRepository;
import com.piloterr.android.piloterr.data.local.implementation.RealmUserLocalRepository;
import com.piloterr.android.piloterr.helpers.AppConfigManager;
import com.piloterr.android.piloterr.helpers.UserScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class UserRepositoryModule {

    @Provides
    SetupCustomizationRepository providesSetupCustomizationRepository(Context context) {
        return new SetupCustomizationRepositoryImpl(context);
    }

    @Provides
    TaskLocalRepository providesTaskLocalRepository(Realm realm) {
        return new RealmTaskLocalRepository(realm);
    }

    @Provides
    @UserScope
    TaskRepository providesTaskRepository(TaskLocalRepository localRepository, ApiClient apiClient, @Named(AppModule.NAMED_USER_ID) String userId, AppConfigManager appConfigManager) {
        return new TaskRepositoryImpl(localRepository, apiClient, userId, appConfigManager);
    }

    @Provides
    TagLocalRepository providesTagLocalRepository(Realm realm) {
        return new RealmTagLocalRepository(realm);
    }

    @Provides
    TagRepository providesTagRepository(TagLocalRepository localRepository, ApiClient apiClient, @Named(AppModule.NAMED_USER_ID) String userId) {
        return new TagRepositoryImpl(localRepository, apiClient, userId);
    }

    @Provides
    ChallengeLocalRepository provideChallengeLocalRepository(Realm realm){
        return new RealmChallengeLocalRepository(realm);
    }

    @Provides
    ChallengeRepository providesChallengeRepository(ChallengeLocalRepository localRepository, ApiClient apiClient, @Named(AppModule.NAMED_USER_ID) String userId) {
        return new ChallengeRepositoryImpl(localRepository, apiClient, userId);
    }

    @Provides
    UserLocalRepository providesUserLocalRepository(Realm realm) {
        return new RealmUserLocalRepository(realm);
    }

    @Provides
    UserRepository providesUserRepository(UserLocalRepository localRepository, ApiClient apiClient, @Named(AppModule.NAMED_USER_ID) String userId, TaskRepository taskRepository, AppConfigManager appConfigManager) {
        return new UserRepositoryImpl(localRepository, apiClient, userId, taskRepository, appConfigManager);
    }

    @Provides
    SocialLocalRepository providesSocialLocalRepository(Realm realm) {
        return new RealmSocialLocalRepository(realm);
    }

    @Provides
    SocialRepository providesSocialRepository(SocialLocalRepository localRepository, ApiClient apiClient, @Named(AppModule.NAMED_USER_ID) String userId) {
        return new SocialRepositoryImpl(localRepository, apiClient, userId);
    }

    @Provides
    InventoryLocalRepository providesInventoryLocalRepository(Realm realm, Context context) {
        return new RealmInventoryLocalRepository(realm, context);
    }

    @Provides
    InventoryRepository providesInventoryRepository(InventoryLocalRepository localRepository, ApiClient apiClient, @Named(AppModule.NAMED_USER_ID) String userId, AppConfigManager remoteConfig) {
        return new InventoryRepositoryImpl(localRepository, apiClient, userId, remoteConfig);
    }

    @Provides
    FAQLocalRepository providesFAQLocalRepository(Realm realm) {
        return new RealmFAQLocalRepository(realm);
    }

    @Provides
    FAQRepository providesFAQRepository(FAQLocalRepository localRepository, ApiClient apiClient, @Named(AppModule.NAMED_USER_ID) String userId) {
        return new FAQRepositoryImpl(localRepository, apiClient, userId);
    }

    @Provides
    TutorialLocalRepository providesTutorialLocalRepository(Realm realm) {
        return new RealmTutorialLocalRepository(realm);
    }

    @Provides
    TutorialRepository providesTutorialRepository(TutorialLocalRepository localRepository, ApiClient apiClient, @Named(AppModule.NAMED_USER_ID) String userId) {
        return new TutorialRepositoryImpl(localRepository, apiClient, userId);
    }

    @Provides
    CustomizationLocalRepository providesCustomizationLocalRepository(Realm realm) {
        return new RealmCustomizationLocalRepository(realm);
    }

    @Provides
    CustomizationRepository providesCustomizationRepository(CustomizationLocalRepository localRepository, ApiClient apiClient, @Named(AppModule.NAMED_USER_ID) String userId) {
        return new CustomizationRepositoryImpl(localRepository, apiClient, userId);
    }
}
