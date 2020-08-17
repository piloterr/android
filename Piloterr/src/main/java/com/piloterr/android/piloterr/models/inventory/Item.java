package com.piloterr.android.piloterr.models.inventory;

import io.realm.RealmModel;

public interface Item extends RealmModel {

    String getType();

    String getKey();

    String getText();

    Integer getValue();
}
