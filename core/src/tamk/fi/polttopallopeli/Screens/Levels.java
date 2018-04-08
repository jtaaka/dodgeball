package tamk.fi.polttopallopeli.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import tamk.fi.polttopallopeli.CampaignLevels.Level1;
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

public class Levels implements Screen {
    private SpriteBatch batch;
    private Dodgeball host;
    private Stage stage;
    private Skin levelSkin;

    private Button level1;
    private Button level2;
    private Button level3;
    private Button level4;
    private Button level5;
    private Button level6;
    private Button level7;
    private Button level8;
    private Button level9;
    private Button level10;
    private Button pointer;

    private Button lock2;
    private Button lock3;
    private Button lock4;
    private Button lock5;
    private Button lock6;
    private Button lock7;
    private Button lock8;
    private Button lock9;
    private Button lock10;

    final int colWidth = Gdx.graphics.getWidth() / 12;
    final int rowHeight = Gdx.graphics.getHeight() / 12;
    final float WIDTH = Gdx.graphics.getWidth();
    final float HEIGHT = Gdx.graphics.getHeight();

    public Levels (Dodgeball host) {
        this.host = host;
        batch = host.getBatch();

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
    }
}
