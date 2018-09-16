package com.example.flo.volumeswitchwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.flo.volumeswitchwidget.util.VolumeSwitchWidgetPreferences;

public class ExampleAppWidgetProvider extends AppWidgetProvider {

    private static final String LOG_TAG = ExampleAppWidgetProvider.class.getSimpleName();

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, ExampleActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.example_appwidget);
            if(VolumeSwitchWidgetPreferences.isMuted(context)){
                Log.d(LOG_TAG, "Showing volume icon");
                views.setImageViewResource(R.id.button, R.drawable.volume);
            } else {
                Log.d(LOG_TAG, "Showing volume OFF icon");
                views.setImageViewResource(R.id.button, R.drawable.volume_off);
            }
            views.setOnClickPendingIntent(R.id.button, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}