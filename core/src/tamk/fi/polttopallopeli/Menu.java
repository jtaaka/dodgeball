package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Class that has all the menu buttons.
 */
public class Menu implements Screen {
    private Texture background;
    private Texture playButton;
    private Texture settingsButton;
    private Texture creditsButton;
    private Dodgeball host;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Rectangle playRectangle;
    private Rectangle settingsRectangle;
    private Rectangle creditsRectangle;
    private Vector3 touchPos;

    public Menu(Dodgeball host) {
        background = new Texture("background.jpg");
        playButton = new Texture("playbutton.png");
        settingsButton = new Texture("settings.jpg");
        creditsButton = new Texture("credits.jpg");

        this.host = host;
        batch = host.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Dodgeball.WORLD_WIDTH, Dodgeball.WORLD_HEIGHT);

        playRectangle = new Rectangle(Dodgeball.WORLD_WIDTH / 2f - playButton.getWidth() / 2f,
                Dodgeball.WORLD_HEIGHT / 2f - playButton.getHeight() / 2f, playButton.getWidth(),
                playButton.getHeight());

        settingsRectangle = new Rectangle(Dodgeball.WORLD_WIDTH / 2f - settingsButton.getWidth() / 2f,
                Dodgeball.WORLD_HEIGHT / 4f - settingsButton.getHeight() / 2f, settingsButton.getWidth(),
                settingsButton.getHeight());

        creditsRectangle = new Rectangle(Dodgeball.WORLD_WIDTH / 2f - creditsButton.getWidth() / 2f,
                Dodgeball.WORLD_HEIGHT / 8f - creditsButton.getHeight() / 2f, creditsButton.getWidth(),
                creditsButton.getHeight());

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

            if (settingsRectangle.contains(touchPos.x, touchPos.y)) {
                host.setScreen(new Settings(host));
            }

            if (creditsRectangle.contains(touchPos.x, touchPos.y)) {
                host.setScreen(new Credits(host));
            }
        }

        batch.begin();
        batch.draw(background, 0, 0,  Dodgeball.WORLD_WIDTH, Dodgeball.WORLD_HEIGHT);

        batch.draw(playButton, Dodgeball.WORLD_WIDTH / 2f - playButton.getWidth() / 2f,
                Dodgeball.WORLD_HEIGHT / 2f - playButton.getHeight() / 2f,
                playButton.getWidth(), playButton.getHeight());

        batch.draw(settingsButton, Dodgeball.WORLD_WIDTH / 2f - settingsButton.getWidth() / 2f,
                Dodgeball.WORLD_HEIGHT / 4f - settingsButton.getHeight() / 2f,
                settingsButton.getWidth(), settingsButton.getHeight());

        batch.draw(creditsButton, Dodgeball.WORLD_WIDTH / 2f - creditsButton.getWidth() / 2f,
                Dodgeball.WORLD_HEIGHT / 8f - creditsButton.getHeight() / 2f,
                creditsButton.getWidth(), creditsButton.getHeight());

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
