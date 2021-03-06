package com.piloterr.android.piloterr.models;


import androidx.annotation.Nullable;

import com.piloterr.android.piloterr.models.user.Outfit;
import com.piloterr.android.piloterr.models.user.Stats;

/**
 * Created by phillip on 29.06.17.
 */

public interface Avatar {
    @Nullable
    String getCurrentMount();

    @Nullable
    String getCurrentPet();

    boolean getSleep();

    @Nullable
    Stats getStats();

    @Nullable
    AvatarPreferences getPreferences();

    @Nullable
    Integer getGemCount();

    @Nullable
    Integer getHourglassCount();

    @Nullable
    Outfit getCostume();
    @Nullable
    Outfit getEquipped();

    boolean hasClass();

    boolean isValid();
}
