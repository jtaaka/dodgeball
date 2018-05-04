package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Class for settings preferences.
 *
 * @author  Juho Taakala <juho.taakala@cs.tamk.fi>
 *          Joni Alanko <joni.alanko@cs.tamk.fi>
 * @since   2018.0222
 * @version 1.0
 */
public class SettingsPreferences {

    /**
     * Defines calibration preferences.
     */
    public static Preferences prefs;

    /**
     * Gets calibration settings.
     */
    public static void getSettings() {
        prefs = Gdx.app.getPreferences("SettingsPreferences");

        prefs.getFloat("calibrationZPositive", 2f);
        prefs.getFloat("calibrationZNegative", 2f);
        prefs.getFloat("calibrationXPositive", 2f);
        prefs.getFloat("calibrationXNegative", 2f);

    }
}
