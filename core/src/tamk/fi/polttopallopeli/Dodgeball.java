package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Dodgeball extends Game {
	private SpriteBatch batch;
	public static final float WORLD_WIDTH = 1270;
    public static final float WORLD_HEIGHT = 720;

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
		batch.begin();
		//batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
