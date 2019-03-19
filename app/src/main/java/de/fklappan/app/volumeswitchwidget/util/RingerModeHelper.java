package de.fklappan.app.volumeswitchwidget.util;

import android.media.AudioManager;

import java.util.HashMap;
import java.util.Map;

public class RingerModeHelper {

    private static Map<Integer, String> modeMap = new HashMap<>();

    public RingerModeHelper() {
        modeMap.put(AudioManager.RINGER_MODE_SILENT, "RINGER_MODE_SILENT");
        modeMap.put(AudioManager.RINGER_MODE_VIBRATE, "RINGER_MODE_VIBRATE");
        modeMap.put(AudioManager.RINGER_MODE_NORMAL, "RINGER_MODE_NORMAL");
    }

    public String getReadableRingerMode(int mode) {
        return modeMap.get(mode);
    }
}
