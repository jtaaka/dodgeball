package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SettingsPreferences {
    public static float calibrationZ[] = new float[10];
    public static Preferences prefs;
    public static Float calibration = 10f;

    public static void setSettings () {
        prefs = Gdx.app.getPreferences("SettingsPreferences");
        prefs.putFloat("calibration", 10);
        prefs.putFloat("calibration1", 1);
        prefs.putFloat("calibration2", 2);
        prefs.putFloat("calibration3", 3);
        prefs.putFloat("calibration4", 4);
        prefs.putFloat("calibration5", 5);
        prefs.putFloat("calibration6", 6);
        prefs.putFloat("calibration7", 7);
        prefs.putFloat("calibration8", 8);
        prefs.putFloat("calibration9", 9);
        prefs.putFloat("calibration10", 10);
        prefs.flush();
    }

    public static void getSettings() {
        prefs = Gdx.app.getPreferences("SettingsPreferences");
        calibration = prefs.getFloat("calibration",0);
        calibrationZ[0] = prefs.getFloat("calibration1",0);
        calibrationZ[1] = prefs.getFloat("calibration1",0);
        calibrationZ[2] = prefs.getFloat("calibration1",0);
        calibrationZ[3] = prefs.getFloat("calibration1",0);
        calibrationZ[4] = prefs.getFloat("calibration1",0);
        calibrationZ[5] = prefs.getFloat("calibration1",0);
        calibrationZ[6] = prefs.getFloat("calibration1",0);
        calibrationZ[7] = prefs.getFloat("calibration1",0);
        calibrationZ[8] = prefs.getFloat("calibration1",0);
        calibrationZ[9] = prefs.getFloat("calibration1",0);
        calibrationZ[10] = prefs.getFloat("calibration1",0);

    }
}
