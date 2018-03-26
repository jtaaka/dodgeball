package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

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
        background = new Texture("dodgeball.jpg");
        playButton = new Texture("Pelaa.png");
        settingsButton = new Texture("Asetukset.png");
        creditsButton = new Texture("Tilastot.png");

        this.host = host;
        batch = host.getBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Dodgeball.WORLD_WIDTH, Dodgeball.WORLD_HEIGHT);

        playRectangle = new Rectangle(Dodgeball.WORLD_WIDTH / 4f - playButton.getWidth() / 200f,
                Dodgeball.WORLD_HEIGHT / 2f - playButton.getHeight() / 200f, playButton.getWidth() / 100f,
                playButton.getHeight() / 100f);

        settingsRectangle = new Rectangle(Dodgeball.WORLD_WIDTH * 0.85f - settingsButton.getWidth() / 200f,
                Dodgeball.WORLD_HEIGHT / 3f - settingsButton.getHeight() / 200f, settingsButton.getWidth() / 100f,
                settingsButton.getHeight() / 100f);

        creditsRectangle = new Rectangle(Dodgeball.WORLD_WIDTH * 0.85f - creditsButton.getWidth() / 200f,
                Dodgeball.WORLD_HEIGHT / 8f - creditsButton.getHeight() / 200f, creditsButton.getWidth() / 100f,
                creditsButton.getHeight() / 100f);

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
        batch.setProjectionMatrix(camera.combined);

        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            touchPos = camera.unproject(touchPos);

            if (playRectangle.contains(touchPos.x, touchPos.y)) {
                host.setScreen(new SurvivalMode(host));
            }

            if (settingsRectangle.contains(touchPos.x, touchPos.y)) {
                host.setScreen(new Settings(host));
            }

            if (creditsRectangle.contains(touchPos.x, touchPos.y)) {
                host.setScreen(new Credits(host));
            }
        }

        // This is for testing the survival mode
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            host.setScreen(new GameScreen(host));
        }

        batch.begin();
        batch.draw(background, 0, 0,  Dodgeball.WORLD_WIDTH, Dodgeball.WORLD_HEIGHT);

        batch.draw(playButton, Dodgeball.WORLD_WIDTH / 4f - playButton.getWidth() / 200f,
                Dodgeball.WORLD_HEIGHT / 2f - playButton.getHeight() / 200f,
                playButton.getWidth() / 100f, playButton.getHeight() / 100f);

        batch.draw(settingsButton, Dodgeball.WORLD_WIDTH * 0.85f - settingsButton.getWidth() / 200f,
                Dodgeball.WORLD_HEIGHT / 3f - settingsButton.getHeight() / 200f,
                settingsButton.getWidth() / 100f, settingsButton.getHeight() / 100f);

        batch.draw(creditsButton, Dodgeball.WORLD_WIDTH * 0.85f - creditsButton.getWidth() / 200f,
                Dodgeball.WORLD_HEIGHT / 8f - creditsButton.getHeight() / 200f,
                creditsButton.getWidth() / 100f, creditsButton.getHeight() / 100f);

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
        dispose();
    }

    @Override
    public void dispose() {
        background.dispose();
        playButton.dispose();
        settingsButton.dispose();
        creditsButton.dispose();
    }
}
