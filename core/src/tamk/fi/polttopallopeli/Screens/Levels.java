package tamk.fi.polttopallopeli.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import tamk.fi.polttopallopeli.CampaignLevels.Level1;
import tamk.fi.polttopallopeli.CampaignLevels.Level11;
import tamk.fi.polttopallopeli.CampaignLevels.Level2;
import tamk.fi.polttopallopeli.CampaignLevels.Level3;
import tamk.fi.polttopallopeli.CampaignLevels.Level4;
import tamk.fi.polttopallopeli.CampaignLevels.Level5;
import tamk.fi.polttopallopeli.CampaignLevels.Level6;
import tamk.fi.polttopallopeli.CampaignLevels.Level7;
import tamk.fi.polttopallopeli.CampaignLevels.Level8;
import tamk.fi.polttopallopeli.CampaignLevels.Level9;
import tamk.fi.polttopallopeli.CampaignLevels.Level10;
import tamk.fi.polttopallopeli.Dodgeball;
import tamk.fi.polttopallopeli.LevelPreferences;

/**
 * A class for levels screen.
 *
 * @author  Juho Taakala <juho.taakala@cs.tamk.fi>
 *          Joni Alanko <joni.alanko@cs.tamk.fi>
 * @since   2018.0222
 * @version 1.0
 */
public class Levels implements Screen {

    /**
     * Defines SpriteBatch for levels screen.
     */
    private SpriteBatch batch;

    /**
     * Defines "main class" as a host.
     */
    private Dodgeball host;

    /**
     * Defines stage for the level.
     */
    private Stage stage;

    /**
     * Defines a skin for button styles.
     */
    private Skin levelSkin;

    /**
     * Defines background texture for world.
     */
    private Texture background;

    /**
     * Defines secret level texture.
     */
    private Texture secret;

    /**
     * Defines a recrangle for the secret level button.
     */
    private Rectangle secretRect;

    /**
     * Defines a button for the level.
     */
    private Button level1;

    /**
     * Defines a button for the level.
     */
    private Button level2;

    /**
     * Defines a button for the level.
     */
    private Button level3;

    /**
     * Defines a button for the level.
     */
    private Button level4;

    /**
     * Defines a button for the level.
     */
    private Button level5;

    /**
     * Defines a button for the level.
     */
    private Button level6;

    /**
     * Defines a button for the level.
     */
    private Button level7;

    /**
     * Defines a button for the level.
     */
    private Button level8;

    /**
     * Defines a button for the level.
     */
    private Button level9;

    /**
     * Defines a button for the level.
     */
    private Button level10;

    /**
     * Defines a back button.
     */
    private Button pointer;

    /**
     * Defines a lock button for the level.
     */
    private Button lock2;

    /**
     * Defines a lock button for the level.
     */
    private Button lock3;

    /**
     * Defines a lock button for the level.
     */
    private Button lock4;

    /**
     * Defines a lock button for the level.
     */
    private Button lock5;

    /**
     * Defines a lock button for the level.
     */
    private Button lock6;

    /**
     * Defines a lock button for the level.
     */
    private Button lock7;

    /**
     * Defines a lock button for the level.
     */
    private Button lock8;

    /**
     * Defines a lock button for the level.
     */
    private Button lock9;

    /**
     * Defines a lock button for the level.
     */
    private Button lock10;

    /**
     * Defines a vector for touch position.
     */
    private Vector3 touchPos;

    /**
     * Defines a lock button for the level.
     */
    private OrthographicCamera camera;

    /**
     * Defines device's screen column width to help position buttons.
     */
    private final int colWidth = Gdx.graphics.getWidth() / 12;

    /**
     * Defines device's screen row height to help position buttons.
     */
    private final int rowHeight = Gdx.graphics.getHeight() / 12;

    /**
     * Defines device's screen width.
     */
    private final float WIDTH = Gdx.graphics.getWidth();

    /**
     * Defines device's screen height.
     */
    private final float HEIGHT = Gdx.graphics.getHeight();

    /**
     * Constructor for levels screen.
     *
     * @param host "main class" host.
     */
    public Levels (Dodgeball host) {
        this.host = host;
        batch = host.getBatch();
        background = new Texture("levelsbg.png");
        secret = new Texture("skullbutton.png");
        secretRect = new Rectangle(WIDTH / 2f - secret.getWidth() / 4f / 2f , HEIGHT * 0.6f,
                secret.getWidth() / 4f, secret.getHeight() / 4f);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);
        touchPos = new Vector3();

        stage = new Stage(new ScreenViewport(), batch);

        levelSkin = new Skin(Gdx.files.internal("levels.json"));
        levelSkin.getFont("showg").getData().setScale(1.5f);

        pointer = new Button(levelSkin,"pointer");
        pointer.setSize(colWidth, rowHeight);
        pointer.setPosition(colWidth * 0.6f, rowHeight);

        lock2 = new Button(levelSkin, "lock");
        lock2.setSize(colWidth, rowHeight * 1.3f);

        lock3 = new Button(levelSkin, "lock");
        lock3.setSize(colWidth, rowHeight * 1.3f);

        lock4 = new Button(levelSkin, "lock");
        lock4.setSize(colWidth, rowHeight * 1.3f);

        lock5 = new Button(levelSkin, "lock");
        lock5.setSize(colWidth, rowHeight * 1.3f);

        lock6 = new Button(levelSkin, "lock");
        lock6.setSize(colWidth, rowHeight * 1.3f);

        lock7 = new Button(levelSkin, "lock");
        lock7.setSize(colWidth, rowHeight * 1.3f);

        lock8 = new Button(levelSkin, "lock");
        lock8.setSize(colWidth, rowHeight * 1.3f);

        lock9 = new Button(levelSkin, "lock");
        lock9.setSize(colWidth, rowHeight * 1.3f);

        lock10 = new Button(levelSkin, "lock");
        lock10.setSize(colWidth, rowHeight * 1.3f);

        level1 = new TextButton("1", levelSkin, "default");
        level1.setSize(colWidth, rowHeight * 1.3f);
        level1.setPosition(colWidth * 0.6f, (HEIGHT / 2f) - level1.getHeight() / 2f);

        level2 = new TextButton("2", levelSkin, "default");
        level2.setSize(colWidth, rowHeight * 1.3f);

        level3 = new TextButton("3", levelSkin, "default");
        level3.setSize(colWidth, rowHeight * 1.3f);

        level4 = new TextButton("4", levelSkin, "default");
        level4.setSize(colWidth, rowHeight * 1.3f);

        level5 = new TextButton("5", levelSkin, "default");
        level5.setSize(colWidth, rowHeight * 1.3f);

        level6 = new TextButton("6", levelSkin, "default");
        level6.setSize(colWidth, rowHeight * 1.3f);

        level7 = new TextButton("7", levelSkin, "default");
        level7.setSize(colWidth, rowHeight * 1.3f);

        level8 = new TextButton("8", levelSkin, "default");
        level8.setSize(colWidth, rowHeight * 1.3f);

        level9 = new TextButton("9", levelSkin, "default");
        level9.setSize(colWidth, rowHeight * 1.3f);

        level10 = new TextButton("10", levelSkin, "default");
        level10.setSize(colWidth, rowHeight * 1.3f);
    }

    @Override
    public void show() {

        LevelPreferences.getStatus();
        LevelPreferences.setStatus();

        pointer.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                host.setScreen(new Menu(host));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        level1.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                host.setScreen(new Level1(host));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        level2.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                host.setScreen(new Level2(host));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        level3.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                host.setScreen(new Level3(host));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        level4.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                host.setScreen(new Level4(host));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        level5.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                host.setScreen(new Level5(host));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        level6.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                host.setScreen(new Level6(host));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        level7.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                host.setScreen(new Level7(host));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        level8.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                host.setScreen(new Level8(host));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        level9.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                host.setScreen(new Level9(host));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        level10.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                host.setScreen(new Level10(host));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        stage.addActor(level1);
        stage.addActor(pointer);

        if (LevelPreferences.level[1] == 0) {
            lock2.setPosition(colWidth * 1.7f, (HEIGHT / 2f) - lock2.getHeight() / 2f);
            stage.addActor(lock2);
        } else {
            level2.setPosition(colWidth * 1.7f, (HEIGHT / 2f) - level2.getHeight() / 2f);
            stage.addActor(level2);
        }

        if (LevelPreferences.level[2] == 0) {
            lock3.setPosition(colWidth * 2.8f, (HEIGHT / 2f) - lock3.getHeight() / 2f);
            stage.addActor(lock3);
        } else {
            level3.setPosition(colWidth * 2.8f, (HEIGHT / 2f) - level3.getHeight() / 2f);
            stage.addActor(level3);
        }

        if (LevelPreferences.level[3] == 0) {
            lock4.setPosition(colWidth * 3.9f, (HEIGHT / 2f) - lock4.getHeight() / 2f);
            stage.addActor(lock4);
        } else {
            level4.setPosition(colWidth * 3.9f, (HEIGHT / 2f) - level4.getHeight() / 2f);
            stage.addActor(level4);
        }

        if (LevelPreferences.level[4] == 0) {
            lock5.setPosition(colWidth * 5f, (HEIGHT / 2f) - lock5.getHeight() / 2f);
            stage.addActor(lock5);
        } else {
            level5.setPosition(colWidth * 5f, (HEIGHT / 2f) - level5.getHeight() / 2f);
            stage.addActor(level5);
        }

        if (LevelPreferences.level[5] == 0) {
            lock6.setPosition(colWidth * 6.1f, (HEIGHT / 2f) - lock6.getHeight() / 2f);
            stage.addActor(lock6);
        } else {
            level6.setPosition(colWidth * 6.1f, (HEIGHT / 2f) - level6.getHeight() / 2f);
            stage.addActor(level6);
        }

        if (LevelPreferences.level[6] == 0) {
            lock7.setPosition(colWidth * 7.2f, (HEIGHT / 2f) - lock7.getHeight() / 2f);
            stage.addActor(lock7);
        } else {
            level7.setPosition(colWidth * 7.2f, (HEIGHT / 2f) - level7.getHeight() / 2f);
            stage.addActor(level7);
        }

        if (LevelPreferences.level[7] == 0) {
            lock8.setPosition(colWidth * 8.3f, (HEIGHT / 2f) - lock8.getHeight() / 2f);
            stage.addActor(lock8);
        } else {
            level8.setPosition(colWidth * 8.3f, (HEIGHT / 2f) - level8.getHeight() / 2f);
            stage.addActor(level8);
        }

        if (LevelPreferences.level[8] == 0) {
            lock9.setPosition(colWidth * 9.4f, (HEIGHT / 2f) - lock9.getHeight() / 2f);
            stage.addActor(lock9);
        } else {
            level9.setPosition(colWidth * 9.4f, (HEIGHT / 2f) - level9.getHeight() / 2f);
            stage.addActor(level9);
        }

        if (LevelPreferences.level[9] == 0) {
            lock10.setPosition(colWidth * 10.5f, (HEIGHT / 2f) - lock10.getHeight() / 2f);
            stage.addActor(lock10);
        } else {
            level10.setPosition(colWidth * 10.5f, (HEIGHT / 2f) - level10.getHeight() / 2f);
            stage.addActor(level10);
        }

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, WIDTH, HEIGHT);
        if (LevelPreferences.level[10] != 0) {
            batch.draw(secret, WIDTH / 2f - secret.getWidth() / 4f / 2f, HEIGHT * 0.6f,
                    secret.getWidth() / 4f, secret.getHeight() / 4f);
        }
        batch.end();

        if (LevelPreferences.level[10] != 0) {
            if (Gdx.input.isTouched()) {
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                touchPos = camera.unproject(touchPos);

                if (secretRect.contains(touchPos.x, touchPos.y)) {
                    host.setScreen(new Level11(host));
                }
            }
        }

        stage.act();
        stage.draw();
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
        stage.dispose();
        levelSkin.dispose();
        background.dispose();
    }
}
