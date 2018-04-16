package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import tamk.fi.polttopallopeli.Screens.Settings;

public class ProfilePreferences {
    public static String name[] = new String[10];
    public static Preferences prefs;

    public static void setProfile() {
        prefs = Gdx.app.getPreferences("ProfilePreferences");
        prefs.putString("profile0", name[0]);
        prefs.putString("profile1", name[1]);
        prefs.putString("profile2", name[2]);
        prefs.putString("profile3", name[3]);
        prefs.putString("profile4", name[4]);
        prefs.putString("profile5", name[5]);
        prefs.putString("profile6", name[6]);
        prefs.putString("profile7", name[7]);
        prefs.putString("profile8", name[8]);
        prefs.putString("profile9", name[9]);
        prefs.flush();
    }

    public static void getprofile() {
        prefs = Gdx.app.getPreferences("ProfilePreferences");
        name[0] = prefs.getString("profile0", "Anonymous");
        name[1] = prefs.getString("profile1", "Anonymous");
        name[2] = prefs.getString("profile2", "Anonymous");
        name[3] = prefs.getString("profile3", "Anonymous");
        name[4] = prefs.getString("profile4", "Anonymous");
        name[5] = prefs.getString("profile5", "Anonymous");
        name[6] = prefs.getString("profile6", "Anonymous");
        name[7] = prefs.getString("profile7", "Anonymous");
        name[8] = prefs.getString("profile8", "Anonymous");
        name[9] = prefs.getString("profile9", "Anonymous");
    }
}
