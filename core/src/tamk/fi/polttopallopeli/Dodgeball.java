package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Dodgeball extends Game {
	private SpriteBatch batch;
	public static final float WINDOW_WIDTH = 1280;
    public static final float WINDOW_HEIGHT = 800;

    public static final float WORLD_WIDTH = 12.8f;
	public static final float WORLD_HEIGHT = 8.0f;

	public static final short OBJECT_WALL = 1;
    public static final short OBJECT_PLAYER = 2;



    @Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new Menu(this));
	}

	public SpriteBatch getBatch() {
	    return batch;
    }

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
