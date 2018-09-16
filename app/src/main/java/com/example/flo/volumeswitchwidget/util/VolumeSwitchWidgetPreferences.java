package com.example.flo.volumeswitchwidget.util;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class VolumeSwitchWidgetPreferences {
    public static final String SHARED_PREF_NAME = "VolumeSwitchWidgetPreferences";
    public static final String SHARED_PREF_LAST_VOLUME = "LastVolume";

    public static boolean isMuted(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        return !prefs.contains(SHARED_PREF_LAST_VOLUME);
    }
}
