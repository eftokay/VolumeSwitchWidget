package de.fklappan.app.volumeswitchwidget.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;

import static android.content.Context.MODE_PRIVATE;

/**
 * The {@link android.content.SharedPreferences} wrapper for save and restore the system stream volume
 */
public class VolumeSwitchWidgetPreferences {
    private static final String LOG_TAG = VolumeSwitchWidgetPreferences.class.getSimpleName();
    public static final String SHARED_PREF_NAME = "VolumeSwitchWidgetPreferences";
    public static final String SHARED_PREF_LAST_VOLUME = "LastVolume";

    public static boolean isMuted(Context context) {
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == 0;
    }

    public static int loadLastVolumeIndex(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        if (!prefs.contains(SHARED_PREF_LAST_VOLUME)) {
            AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
            int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            return volume > 0 ? volume : 5;
        }
        return prefs.getInt(SHARED_PREF_LAST_VOLUME, 5);
    }

    public static void saveLastVolumeIndex(Context context, final int volume) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        prefs.edit().putInt(SHARED_PREF_LAST_VOLUME, volume).apply();
    }

}
