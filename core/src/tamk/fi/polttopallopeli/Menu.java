package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Menu implements Screen {
    private Texture playButton;
    private Dodgeball host;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Rectangle playRectangle;
    private Vector3 touchPos;

    public Menu(Dodgeball host) {
        playButton = new Texture("playbutton.png");
        this.host = host;
        batch = host.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Dodgeball.WORLD_WIDTH, Dodgeball.WORLD_HEIGHT);

        playRectangle = new Rectangle(Dodgeball.WORLD_WIDTH / 2f - playButton.getWidth() / 2f,
                Dodgeball.WORLD_HEIGHT / 2f - playButton.getHeight() / 2f, playButton.getWidth(),
                playButton.getHeight());

        touchPos = new Vector3();
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            touchPos = camera.unproject(touchPos);

            if (playRectangle.contains(touchPos.x, touchPos.y)) {
                host.setScreen(new GameScreen(host));
            }
        }

        batch.begin();
        batch.draw(playButton, Dodgeball.WORLD_WIDTH / 2f - playButton.getWidth() / 2f,
                Dodgeball.WORLD_HEIGHT / 2f - playButton.getHeight() / 2f,
                playButton.getWidth(), playButton.getHeight());
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
