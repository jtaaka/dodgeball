package tamk.fi.polttopallopeli.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import tamk.fi.polttopallopeli.Dodgeball;

public class Levels implements Screen {
    private SpriteBatch batch;
    private Dodgeball host;
    private Texture background;
    private OrthographicCamera camera;

    private TextureAtlas atlas;
    private TextureRegion arrow;
    private TextureRegion levels[] = new TextureRegion[10];
    private Array<AtlasRegion> atlasArray;

    public static int level[] = new int[10];
    public static Preferences prefs;
    public static Integer levelclear;

    public Levels (Dodgeball host) {
        this.host = host;
        batch = host.getBatch();

        background = new Texture("levelsbg.png");

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Dodgeball.WORLD_WIDTH, Dodgeball.WORLD_HEIGHT);

        atlas = new TextureAtlas("levels.atlas");
        atlasArray = new Array<AtlasRegion>(atlas.getRegions());
    }

    public static void putStatus() {
        prefs = Gdx.app.getPreferences("LevelsPreferences");
        prefs.putInteger("levelclear", 10);
        prefs.putInteger("level1", 1); // val 1 = auki
        prefs.putInteger("level2", 2); // val 2 = lukossa
        prefs.putInteger("level3", 2);
        prefs.putInteger("level4", 2);
        prefs.putInteger("level5", 2);
        prefs.putInteger("level6", 2);
        prefs.putInteger("level7", 2);
        prefs.putInteger("level8", 2);
        prefs.putInteger("level9", 2);
        prefs.putInteger("level10",2);
        prefs.flush();
    }

    public static void getStatus() {
        prefs = Gdx.app.getPreferences("LevelsPreferences");
        levelclear = prefs.getInteger("levelclear",0);
        level[0] = prefs.getInteger("level1",0);
        level[1] = prefs.getInteger("level2",0);
        level[2] = prefs.getInteger("level3",0);
        level[3] = prefs.getInteger("level4",0);
        level[4] = prefs.getInteger("level5",0);
        level[5] = prefs.getInteger("level6",0);
        level[6] = prefs.getInteger("level7",0);
        level[7] = prefs.getInteger("level8",0);
        level[8] = prefs.getInteger("level9",0);
        level[9]= prefs.getInteger("level10",0);
    }

    @Override
    public void show() {
        putStatus();
        getStatus();

        arrow = atlasArray.get(11);

        batch.begin();
        batch.draw(background, 0, 0, Dodgeball.WORLD_WIDTH, Dodgeball.WORLD_HEIGHT);
        batch.draw(arrow, 1f, 6.5f, 1f, 0.5f);

        for (int i = 0; i < 10; i++) {

            if (i + 1 <= levelclear) {

                int status = level[i];

                if (status == 1) {
                    levels[i] = atlasArray.get(i);
                    batch.draw(levels[i], 1.5f + i, 4f, 1f, 1f);
                }

                if (status == 2) {
                    levels[i] = atlasArray.get(10);
                    batch.draw(levels[i], 1.5f + i, 4f, 1f, 1f);
                }
            }
        }

        batch.end();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        show();
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
        atlas.dispose();
        background.dispose();
    }
}
