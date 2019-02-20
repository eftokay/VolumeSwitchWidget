package com.example.flo.volumeswitchwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.flo.volumeswitchwidget.util.VolumeSwitchWidgetPreferences;

public class ExampleAppWidgetProvider extends AppWidgetProvider {

    private static final String LOG_TAG = ExampleAppWidgetProvider.class.getSimpleName();
    private static final String TOUCH_ACTION = "com.example.flo.volumeswitchwidget.TOUCH_ACTION";

    private static boolean initObserver = true;

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(LOG_TAG, "onUpdate");

        initVolumeChangedObserver(context);

        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Intent touchIntent = new Intent(context, ExampleAppWidgetProvider.class);
        touchIntent.setAction(TOUCH_ACTION);
        // provide widget id to perform an update on that widget after touch
        touchIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, touchIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.example_appwidget);
        setImageResource(context, views);
        views.setOnClickPendingIntent(R.id.button, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
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
        if (VolumeSwitchWidgetPreferences.isMuted(context)) {
            views.setImageViewResource(R.id.button, R.drawable.volume_off);
        } else {
            views.setImageViewResource(R.id.button, R.drawable.volume);
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

            // NOTE: If we mute the stream through the system UI, we can not unmute it with
            // audioManager.adjustStreamVolume - see adr 0001
            if (VolumeSwitchWidgetPreferences.isMuted(context)) {
                Log.d(LOG_TAG, "unmuting");
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, VolumeSwitchWidgetPreferences.loadLastVolumeIndex(context), AudioManager.FLAG_SHOW_UI);
            } else {
                Log.d(LOG_TAG, "muting");
                VolumeSwitchWidgetPreferences.saveLastVolumeIndex(context, audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_SHOW_UI);
            }

            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            onUpdate(context, AppWidgetManager.getInstance(context), new int[]{appWidgetId});
        }
        super.onReceive(context, intent);
    }
}