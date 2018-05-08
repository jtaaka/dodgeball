package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

import tamk.fi.polttopallopeli.Screens.SplashScreen;

/**
 * Main class.
 *
 * @author  Joni Alanko <joni.alanko@cs.tamk.fi>
 *          Juho Taakala <juho.taakala@cs.tamk.fi>
 * @since   2018.0222
 * @version 1.0
 */
public class Dodgeball extends Game {

    /**
     * Defines SpriteBatch for the game.
     */
    private SpriteBatch batch;

    /**
     * Defines the window width.
     */
    public static final float WINDOW_WIDTH = 1280;

    /**
     * Defines the window height.
     */
    public static final float WINDOW_HEIGHT = 800;

    /**
     * Defines the world width to be used in the game.
     */
    public static final float WORLD_WIDTH = 12.8f;

    /**
     * Defines the world height to be used in the game.
     */
    public static final float WORLD_HEIGHT = 8.0f;

    /**
     * Wall identifier.
     */
	public static final short OBJECT_WALL = 1;

	/**
     * Player identiefier.
     */
    public static final short OBJECT_PLAYER = 2;

    /**
     * Ball identifier.
     */
    public static final short OBJECT_BALL = 4;

    /**
     * Used for sound volume.
     */
    public static float VOLUME = 1.0f;

    /**
     * Used for music volume.
     */
    public static float MUSIC_VOLUME = 1.0f;

    /**
     * Used for music and sound handling.
     */
    public AssetManager manager;

    /**
     * Defines language locale.
     */
    private Locale lang;

    /**
     * Defines language bundle.
     */
    private I18NBundle myBundle;

    @Override
	public void create () {
		batch = new SpriteBatch();

        lang = Locale.getDefault();
        myBundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"), lang);

		manager = new AssetManager();
        manager.load("menu.ogg", Music.class);
        manager.load("survival.ogg", Music.class);
        manager.load("India.ogg", Music.class);
        manager.load("Clucth.ogg", Music.class);
		manager.load("hit.ogg", Sound.class);
		manager.load("hit2.ogg", Sound.class);
		manager.load("highscore.ogg", Sound.class);
		manager.finishLoading();

        setScreen(new SplashScreen(this));
	}

    /**
     * Gets asset manager.
     *
     * @param identifier String asset name.
     * @param type Class type.
     * @param <T> Class type.
     * @return
     */
	public <T> T getManager(String identifier, Class<T> type) {
        return manager.get(identifier, type);
    }

    /**
     * Gets language bundle.
     *
     * @return bundle.
     */
	public I18NBundle getLang() {
        return myBundle;
    }

    /**
     * Gets SpriteBatch.
     *
     * @return SpriteBatch.
     */
    public SpriteBatch getBatch() {
	    return batch;
    }

    /**
     * Mutes sounds.
     */
    public static void muteSounds(){
        VOLUME = 0.0f;
    }

    /**
     * Plays sounds at maximum volume.
     */
    public static void playSounds(){
        VOLUME = 1.0f;
    }

    /**
     * Mutes music.
     */
    public static void muteMusic(){
        MUSIC_VOLUME = 0.0f;
    }

    /**
     * Plays music at maximum volume.
     */
    public static void playMusic(){
        MUSIC_VOLUME = 1.0f;
    }

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
    	setScreen(null);
    	batch.dispose();
    	manager.dispose();
	}
}
