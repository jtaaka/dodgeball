package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SettingsPreferences {
    public static Preferences prefs;

    public static void setSettings () {
        prefs = Gdx.app.getPreferences("SettingsPreferences");
        //prefs.putFloat("calibrationZPositive", 9f);
        //prefs.putFloat("calibrationZNegative", 9f);
        //prefs.putFloat("calibrationXPositive", 9f);
        //prefs.putFloat("calibrationXNegative", 9f);
        prefs.flush();
    }

    public static void getSettings() {
        prefs = Gdx.app.getPreferences("SettingsPreferences");

        prefs.getFloat("calibrationZPositive", 2f);
        prefs.getFloat("calibrationZNegative", 2f);
        prefs.getFloat("calibrationXPositive", 2f);
        prefs.getFloat("calibrationXNegative", 2f);

    }
}
