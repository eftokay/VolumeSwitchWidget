package de.fklappan.app.volumeswitchwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.HashMap;
import java.util.Map;

import de.fklappan.app.volumeswitchwidget.util.RingVolumeSwitchWidgetPreferences;

public class RingVolumeWidgetProvider extends AppWidgetProvider {


    private static final String LOG_TAG = RingVolumeWidgetProvider.class.getSimpleName();
    private static final String TOUCH_ACTION = "RingVolumeWidgetProvider.TOUCH_ACTION";

    private static boolean initObserver = true;

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(LOG_TAG, "onUpdate");

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//        Log.d(LOG_TAG, "Ringermode: " + modeMap.get(audioManager.getRingerMode()));

        if (!RingVolumeSwitchWidgetPreferences.isConfigured(context)) {
            Log.d(LOG_TAG, "Widget is not configured. Start config activity");
            Intent configIntent = new Intent(context, ConfigActivity.class);
            context.startActivity(configIntent);
        }

        initVolumeChangedObserver(context);

        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.music_volume_toggle_widget);
            setImageResource(context, views);

            if (!RingVolumeSwitchWidgetPreferences.isConfigured(context)) {
                setViewConfigClickListener(context, appWidgetIds, views);
                appWidgetManager.updateAppWidget(appWidgetId, views);
                continue;
            }

            updateWidget(views, context, appWidgetManager, appWidgetId);
        }
    }

    private void updateWidget(RemoteViews views , Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Log.d(LOG_TAG, "updating widget");
        if (RingVolumeSwitchWidgetPreferences.isConfigured(context)) {
            setViewClickListener(context, appWidgetId, views);
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private void setViewClickListener(Context context, int appWidgetId, RemoteViews views) {
        Intent touchIntent = new Intent(context, RingVolumeWidgetProvider.class);
        touchIntent.setAction(TOUCH_ACTION);
        // provide widget id to perform an update on that widget after touch
        touchIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, touchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.button, pendingIntent);
    }

    private void setViewConfigClickListener(Context context, int [] appWidgetIds, RemoteViews views) {
        Log.d(LOG_TAG, "setViewConfigClickListener");
        Intent touchIntent = new Intent(context, ConfigActivity.class);
        touchIntent.setAction(TOUCH_ACTION);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, touchIntent, 0);
        views.setOnClickPendingIntent(R.id.button, pendingIntent);

    }

    private void initVolumeChangedObserver(Context context) {
        if (initObserver) {
            initObserver = false;

            context.getApplicationContext().getContentResolver().registerContentObserver(
                    android.provider.Settings.System.CONTENT_URI, true,
                    new VolumeContentObserver(context, new Handler()));
        }
    }

    private void setImageResource(Context context, RemoteViews views) {
        Log.d(LOG_TAG, "setImageResource");
        switch (RingVolumeSwitchWidgetPreferences.getRingerMode(context)) {
            case AudioManager.RINGER_MODE_SILENT:
                views.setImageViewResource(R.id.button, R.drawable.baseline_notifications_off_white_48dp);
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                views.setImageViewResource(R.id.button, R.drawable.baseline_vibration_white_48dp);
                break;
            case AudioManager.RINGER_MODE_NORMAL:
                views.setImageViewResource(R.id.button, R.drawable.baseline_notifications_active_white_48dp);
                break;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(TOUCH_ACTION)) {
            Log.d(LOG_TAG, "onReceive TOUCH_ACTION");

            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (audioManager == null) {
                Log.e(LOG_TAG, "Could not obtain AudioManager instance. Abort");
                return;
            }

            switch (RingVolumeSwitchWidgetPreferences.getRingerMode(context)) {
                case AudioManager.RINGER_MODE_SILENT:
                    Log.d(LOG_TAG, "set RINGER_MODE_NORMAL");
                    setRingerNormal(audioManager);
                    break;
                case AudioManager.RINGER_MODE_VIBRATE:
                    Log.d(LOG_TAG, "set RINGER_MODE_SILENT");
                    setRingerSilent(audioManager);
                    break;
                case AudioManager.RINGER_MODE_NORMAL:
                    Log.d(LOG_TAG, "set RINGER_MODE_VIBRATE");
                    setRingerVibrate(audioManager);
                    break;
            }
//            Log.d(LOG_TAG, "Ringermode: " + modeMap.get(audioManager.getRingerMode()));

            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            onUpdate(context, AppWidgetManager.getInstance(context), new int[]{appWidgetId});
        }
        super.onReceive(context, intent);
    }

    private void setRingerVibrate(AudioManager audioManager) {
        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
    }

    private void setRingerSilent(AudioManager audioManager) {
        // this was the only way to deactivate the vibrate mode, before activating silent mode
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        // due to a strange android bug, this was the only way to get the phone to silent mode
        audioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, 0);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }

    private void setRingerNormal(AudioManager audioManager) {
        audioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_UNMUTE, 0);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }

}