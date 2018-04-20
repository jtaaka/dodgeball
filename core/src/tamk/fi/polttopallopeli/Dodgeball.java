package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
    public static AssetManager manager;

    @Override
	public void create () {
		batch = new SpriteBatch();

		manager = new AssetManager();
		manager.load("hit.ogg", Sound.class);
		manager.load("hit2.ogg", Sound.class);
		manager.finishLoading();

        setScreen(new SplashScreen(this));
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

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
    	setScreen(null);
    	batch.dispose();
	}
}
