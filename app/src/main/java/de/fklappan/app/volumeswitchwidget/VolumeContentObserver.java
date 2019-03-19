package de.fklappan.app.volumeswitchwidget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;

public class VolumeContentObserver extends ContentObserver {

    private static final String LOG_TAG = VolumeContentObserver.class.getSimpleName();
    private Context context;
    private int lastVolume;
    private int lastRingerMode;

    public VolumeContentObserver(Context context, Handler handler) {
        super(handler);
        this.context = context;
        lastVolume = getCurrentVolume(AudioManager.STREAM_MUSIC);
        lastRingerMode = getRingerMode();
    }

    @Override
    public void onChange(boolean selfChange) {
        Log.d(LOG_TAG, "System settings changed");
        super.onChange(selfChange);

        // check if the audio volume changed - other changed system settings wont affect us
        if (soundVolumeChanged()) {
            Log.d(LOG_TAG, "Sound volume changed");
            Intent intent = new Intent(context, MusicVolumeWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context.getApplicationContext(), MusicVolumeWidgetProvider.class));
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            context.sendBroadcast(intent);
        }

        if (ringerModeChanged()) {
            Log.d(LOG_TAG, "Ringer mode changed");
            Intent intent = new Intent(context, RingVolumeWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context.getApplicationContext(), RingVolumeWidgetProvider.class));
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            context.sendBroadcast(intent);
        }
    }

    private int getCurrentVolume(int streamType) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getStreamVolume(streamType);
    }

    private int getRingerMode() {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getRingerMode();
    }

    private boolean soundVolumeChanged() {
        return getCurrentVolume(AudioManager.STREAM_MUSIC) != lastVolume;
    }

    private boolean ringerModeChanged() {
        return getRingerMode() != lastRingerMode;
    }
}
