package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameScreen extends Sprite implements Screen {
    private SpriteBatch batch;
    private Dodgeball host;
    private Texture backgroundGame;
    private OrthographicCamera camera;
    private Animation<TextureRegion> playerAnime;
    private float currentFrameTime;

    public GameScreen (Dodgeball host) {
        super(new Texture("walk.png"));

        TextureRegion[][] tmp = TextureRegion.split(getTexture(), getTexture().getWidth() / 8,
                getTexture().getHeight() / 8);
        TextureRegion[] playerFrames = transformTo1D(tmp);
        playerAnime = new Animation<TextureRegion>(1/10f, playerFrames);

        setSize(getWidth()/8, getHeight()/8);

        currentFrameTime = 0;

        this.host = host;
        batch = host.getBatch();

        backgroundGame = new Texture("game.jpg");
    }

    private TextureRegion[] transformTo1D(TextureRegion[][] tmp) {
        TextureRegion [] playerFrames = new TextureRegion[tmp.length * tmp[0].length];

        int index = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                playerFrames[index++] = tmp[i][j];
            }
        }

        return playerFrames;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Dodgeball.WORLD_WIDTH, Dodgeball.WORLD_HEIGHT);

        currentFrameTime += delta;

        batch.begin();

        batch.draw(backgroundGame,0,0, Dodgeball.WORLD_WIDTH, Dodgeball.WORLD_HEIGHT);

        TextureRegion currentFrame = playerAnime.getKeyFrame(currentFrameTime, true);
        batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());

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
