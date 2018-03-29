package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class LevelPreferences {
    public static int level[] = new int[10];
    public static Preferences prefs;
    public static Integer levelClear;

    public static void setStatus() {
        prefs = Gdx.app.getPreferences("LevelsPreferences");
        prefs.putInteger("levelclear", 10);
        prefs.putInteger("level1", 1); // val 1 = auki, val 0 = lukossa
        prefs.putInteger("level2", prefs.getInteger("level2"));
        prefs.putInteger("level3", prefs.getInteger("level3"));
        prefs.putInteger("level4", prefs.getInteger("level4"));
        prefs.putInteger("level5", prefs.getInteger("level5"));
        prefs.putInteger("level6", prefs.getInteger("level6"));
        prefs.putInteger("level7", prefs.getInteger("level7"));
        prefs.putInteger("level8", prefs.getInteger("level8"));
        prefs.putInteger("level9", prefs.getInteger("level9"));
        prefs.putInteger("level10", prefs.getInteger("level10"));
        prefs.flush();
    }

    public static void getStatus() {
        prefs = Gdx.app.getPreferences("LevelsPreferences");
        levelClear = prefs.getInteger("levelclear",0);
        level[0] = prefs.getInteger("level1",0);
        level[1] = prefs.getInteger("level2",0);
        level[2] = prefs.getInteger("level3",0);
        level[3] = prefs.getInteger("level4",0);
        level[4] = prefs.getInteger("level5",0);
        level[5] = prefs.getInteger("level6",0);
        level[6] = prefs.getInteger("level7",0);
        level[7] = prefs.getInteger("level8",0);
        level[8] = prefs.getInteger("level9",0);
        level[9] = prefs.getInteger("level10",0);
    }
}
