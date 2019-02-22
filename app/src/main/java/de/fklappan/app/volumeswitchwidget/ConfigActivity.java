package de.fklappan.app.volumeswitchwidget;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.fklappan.app.volumeswitchwidget.util.RingVolumeSwitchWidgetPreferences;

/**
 * This activity is used to notify the user about a additional needed permission. It contains a
 * little manual how to grant the permission and opens the permission dialog on click
 */
public class ConfigActivity extends AppCompatActivity {

    private static final int ON_DO_NOT_DISTURB_CALLBACK_CODE = 101;
    private static final String LOG_TAG = ConfigActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        ButterKnife.bind(this);
        setTitle(R.string.grant_permission);
    }

    @OnClick(R.id.mainLayout)
    public void onTapDisplay(View v) {
        requestMutePermissions();
    }

    private void requestMutePermissions() {
        try {
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager.isNotificationPolicyAccessGranted()) {
                RingVolumeSwitchWidgetPreferences.setConfigured(this, true);
                finish();
            } else {
                // Open Setting screen to ask for permisssion
                Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivityForResult(intent, ON_DO_NOT_DISTURB_CALLBACK_CODE);
            }
        } catch (SecurityException e) {
            Log.e(LOG_TAG, "Exception while requesting permissions from user", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ON_DO_NOT_DISTURB_CALLBACK_CODE) {
            if (resultCode == RESULT_OK) {
                requestMutePermissions();
            }
        }
    }
}
