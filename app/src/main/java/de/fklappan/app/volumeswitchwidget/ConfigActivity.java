package de.fklappan.app.volumeswitchwidget;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.flo.volumeswitchwidget.R;

import de.fklappan.app.volumeswitchwidget.util.VolumeSwitchWidgetPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This activity only is used as an informational activity. It notifies the user about a change to his
 * media volume control. It switches between two states: no sound and last set volume by the user.
 * The last set volume is saved to the android preferences.
 */
public class ConfigActivity extends AppCompatActivity {

    @BindView(R.id.button)
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        ButterKnife.bind(this);
        setTitle("");
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void updateButton() {
        if (VolumeSwitchWidgetPreferences.isMuted(this)){
            imageView.setImageResource(R.drawable.volume_off);
        } else {
            imageView.setImageResource(R.drawable.volume);
        }
    }

    @OnClick(R.id.button)
    public void onClickButton(View v) {
        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        if (VolumeSwitchWidgetPreferences.isMuted(this)){
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 10, AudioManager.FLAG_SHOW_UI);
        } else {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_SHOW_UI);

        }

        updateButton();
    }
}
