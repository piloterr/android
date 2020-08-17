package com.piloterr.android.piloterr.models;

import com.piloterr.android.piloterr.models.user.Hair;

/**
 * Created by phillip on 15.09.17.
 */

public interface AvatarPreferences {

    String getUserId();

    Hair getHair();

    boolean getCostume();

    boolean getSleep();

    String getShirt();

    String getSkin();
    String getSize();

    String getBackground();

    String getChair();

    boolean getDisableClasses();
}
