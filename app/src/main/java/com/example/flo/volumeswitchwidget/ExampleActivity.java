package com.example.flo.volumeswitchwidget;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This activity only is used as an informational activity. It notifies the user about a change to his
 * media volume control. It switches between two states: no sound and last set volume by the user.
 * The last set volume is saved to the android preferences.
 */
public class ExampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        ButterKnife.bind(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);

        Toast.makeText(this, "TEST", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.button)
    public void onClickButton(View v) {
        finish();
    }


}
