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

public class Dodgeball extends Game {
	private SpriteBatch batch;
	public static final float WINDOW_WIDTH = 1280;
    public static final float WINDOW_HEIGHT = 800;

    public static final float WORLD_WIDTH = 12.8f;
	public static final float WORLD_HEIGHT = 8.0f;

	public static final short OBJECT_WALL = 1;
    public static final short OBJECT_PLAYER = 2;
    public static final short OBJECT_BALL = 4;

    public static float VOLUME = 1.0f;
    public static float MUSIC_VOLUME = 1.0f;
    public static AssetManager manager;

    private Locale lang;
    private I18NBundle myBundle;

    @Override
	public void create () {
		batch = new SpriteBatch();

        lang = Locale.getDefault();
        myBundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"), lang);

		manager = new AssetManager();
        manager.load("menu.ogg", Music.class);
        manager.load("survival.ogg", Music.class);
		manager.load("hit.ogg", Sound.class);
		manager.load("hit2.ogg", Sound.class);
		manager.load("highscore.ogg", Sound.class);
		manager.finishLoading();

        setScreen(new SplashScreen(this));
	}

	public I18NBundle getLang() {
        return myBundle;
    }

	public SpriteBatch getBatch() {
	    return batch;
    }

    public static void muteSounds(){
        VOLUME = 0.0f;
    }

    public static void playSounds(){
        VOLUME = 1.0f;
    }

    public static void muteMusic(){
        MUSIC_VOLUME = 0.0f;
    }

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
