package com.piloterr.android.piloterr.events;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.piloterr.android.piloterr.ui.views.PiloterrSnackbar;

/**
 * Created by phillip on 26.06.17.
 */

public class ShowSnackbarEvent {

    public Drawable leftImage;
    public String title;
    public String text;
    public PiloterrSnackbar.SnackbarDisplayType type;
    public View specialView;
    public Drawable rightIcon;
    public int rightTextColor;
    public String rightText;
}
