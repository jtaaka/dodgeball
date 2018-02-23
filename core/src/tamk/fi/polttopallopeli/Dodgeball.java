package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Main class that creates batch and gives world parameters.
 */
public class Dodgeball extends Game {
	private SpriteBatch batch;
	public static final float WORLD_WIDTH = 1280;
    public static final float WORLD_HEIGHT = 800;

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
