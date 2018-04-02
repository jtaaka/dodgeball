package tamk.fi.polttopallopeli.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import tamk.fi.polttopallopeli.Dodgeball;
import tamk.fi.polttopallopeli.LevelPreferences;
import tamk.fi.polttopallopeli.LevelTemplate;

public class Levels implements Screen {
    private SpriteBatch batch;
    private Dodgeball host;
    private Texture background;
    private OrthographicCamera camera;

    private TextureAtlas atlas;
    private TextureRegion arrow;
    private TextureRegion levels[] = new TextureRegion[10];
    private Array<AtlasRegion> atlasArray;

    public Levels (Dodgeball host) {
        this.host = host;
        batch = host.getBatch();

        background = new Texture("levelsbg.png");

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Dodgeball.WORLD_WIDTH, Dodgeball.WORLD_HEIGHT);

        atlas = new TextureAtlas("levels.atlas");
        atlasArray = new Array<AtlasRegion>(atlas.getRegions());
        arrow = atlasArray.get(11);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        LevelPreferences.setStatus();
        LevelPreferences.getStatus();

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // For testing purposes
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            host.setScreen(new Menu(host));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            host.setScreen(new LevelTemplate(host));
        }

        batch.begin();
        batch.draw(background, 0, 0, Dodgeball.WORLD_WIDTH, Dodgeball.WORLD_HEIGHT);
        batch.draw(arrow, 1f, 0.5f, 1f, 0.5f);

        for (int i = 0; i < 10; i++) {
            int status = LevelPreferences.level[i];

            if (status == 1) {
                levels[i] = atlasArray.get(i);

            } else if (status == 0) {
                levels[i] = atlasArray.get(10);
            }
            if (levels[i] != null) {
                batch.draw(levels[i], 1f + i * 1.1f, 4f, 1f, 1f);
            }
        }

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
        atlas.dispose();
        background.dispose();
    }
}
