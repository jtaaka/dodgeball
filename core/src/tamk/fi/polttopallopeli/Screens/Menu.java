package tamk.fi.polttopallopeli.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.Locale;

import tamk.fi.polttopallopeli.Dodgeball;
import tamk.fi.polttopallopeli.SurvivalMode;

public class Menu implements Screen {
    private Dodgeball host;
    private SpriteBatch batch;
    private Texture background;
    private Image bgImage;
    private Stage stage;
    private Skin menuSkin;
    private Skin profileSkin;
    public static TextField profile;

    private Button playButton;
    private Button survivalButton;
    private Button levelsButton;
    private Button settingsButton;
    private Button scoreButton;
    private Button exitButton;
    private Button soundOn;
    private Button soundOff;
    private Button musicOn;
    private Button musicOff;
    private Button fin;
    private Button uk;

    private Locale lang;
    private String play;
    private String survival;
    private String levels;
    private String highscore;
    private String settings;
    private String exit;

    final int colWidth = Gdx.graphics.getWidth() / 12;
    final int rowHeight = Gdx.graphics.getHeight() / 12;
    final float WIDTH = Gdx.graphics.getWidth();
    final float HEIGHT = Gdx.graphics.getHeight();

    private Preferences prefs;

    public Menu(Dodgeball host) {
        this.host = host;
        batch = host.getBatch();

        prefs = Gdx.app.getPreferences("currentProfile");

        lang = Locale.getDefault();
        I18NBundle myBundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"), lang);

        play = myBundle.get("play");
        survival = myBundle.get("survival");
        levels = myBundle.get("levels");
        highscore = myBundle.get("highscore");
        settings = myBundle.get("settings");
        exit = myBundle.get("exit");

        background = new Texture(Gdx.files.internal("menubg.png"));
        bgImage = new Image(background);
        bgImage.setSize(WIDTH, HEIGHT);

        stage = new Stage(new ScreenViewport(), batch);

        profileSkin = new Skin(Gdx.files.internal("Holo-dark-xhdpi.json"));

        profile = new TextField(prefs.getString("profile"), profileSkin, "default");
        profile.setMaxLength(10);
        profile.setMessageText("Type name here");
        profile.setSize(colWidth * 2.9f, rowHeight);
        profile.setPosition(colWidth * 8.5f, rowHeight / 2f);

        menuSkin = new Skin(Gdx.files.internal("menu.json"));
        menuSkin.getFont("showg").getData().setScale(1.2f);

        playButton = new TextButton(play, menuSkin,"play");
        playButton.setSize(colWidth * 2, rowHeight * 2);
        playButton.setPosition(colWidth * 1f,HEIGHT / 2 - playButton.getHeight() / 2);

        survivalButton = new TextButton(survival, menuSkin, "default");
        survivalButton.setSize(colWidth * 3f, rowHeight);

        levelsButton = new TextButton(levels, menuSkin, "default");
        levelsButton.setSize(colWidth * 3f, rowHeight);

        settingsButton = new TextButton(settings, menuSkin, "default");
        settingsButton.setSize(colWidth * 2.5f, rowHeight);
        settingsButton.setPosition(WIDTH - settingsButton.getWidth() - colWidth,HEIGHT / 2f);

        scoreButton = new TextButton(highscore, menuSkin, "default");
        scoreButton.setSize(colWidth * 2.5f, rowHeight);
        scoreButton.setPosition(WIDTH - settingsButton.getWidth() - colWidth, rowHeight * 4f);

        exitButton = new TextButton(exit, menuSkin, "default");
        exitButton.setSize(colWidth * 2.5f, rowHeight);
        exitButton.setPosition(WIDTH - settingsButton.getWidth() - colWidth, rowHeight * 2f);

        soundOn = new Button(menuSkin, "soundon");
        soundOn.setSize(colWidth, rowHeight);
        soundOn.setPosition(colWidth * 0.5f, rowHeight / 2f);

        soundOff = new Button(menuSkin, "soundoff");
        soundOff.setSize(colWidth, rowHeight);

        musicOn = new Button(menuSkin, "musicon");
        musicOn.setSize(colWidth * 0.70f, rowHeight);
        musicOn.setPosition(colWidth * 2f, rowHeight / 2f);

        musicOff = new Button(menuSkin, "musicoff");
        musicOff.setSize(colWidth, rowHeight);

        fin = new Button(menuSkin, "fin");
        fin.setSize(colWidth, rowHeight * 1.2f);
        fin.setPosition(colWidth * 4f, rowHeight / 2f);

        uk = new Button(menuSkin, "uk");
        uk.setSize(colWidth, rowHeight * 1.2f);
    }

    @Override
    public void show() {

        playButton.addListener(new InputListener() {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

                survivalButton.setPosition(colWidth * 3.2f,rowHeight * 6.5f);
                levelsButton.setPosition(colWidth * 3.2f,rowHeight * 4.5f);

                stage.addActor(survivalButton);
                stage.addActor(levelsButton);
            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        survivalButton.addListener(new InputListener() {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                host.setScreen(new SurvivalMode(host));
            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        levelsButton.addListener(new InputListener() {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                host.setScreen(new Levels(host));
            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        settingsButton.addListener(new InputListener() {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                host.setScreen(new Settings(host));
            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        scoreButton.addListener(new InputListener() {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                host.setScreen(new HighScore(host));
            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        exitButton.addListener(new InputListener() {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();

            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        soundOn.addListener(new InputListener() {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                soundOn.remove();
                soundOff.setPosition(colWidth * 0.5f, rowHeight / 2f);

                stage.addActor(soundOff);
            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        soundOff.addListener(new InputListener() {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                soundOff.remove();
                soundOn.setPosition(colWidth * 0.5f, rowHeight / 2f);

                stage.addActor(soundOn);
            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        musicOn.addListener(new InputListener() {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                musicOn.remove();
                musicOff.setPosition(colWidth * 2f, rowHeight / 2f);

                stage.addActor(musicOff);

            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        musicOff.addListener(new InputListener() {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                musicOff.remove();
                musicOn.setPosition(colWidth * 2f, rowHeight / 2f);

                stage.addActor(musicOn);
            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        stage.addActor(bgImage);
        stage.addActor(profile);
        stage.addActor(playButton);
        stage.addActor(settingsButton);
        stage.addActor(scoreButton);
        stage.addActor(exitButton);
        stage.addActor(soundOn);
        stage.addActor(musicOn);

        if (Locale.getDefault().getLanguage().equals("fi")) {
            fin.setPosition(colWidth * 4f, rowHeight / 2f);
            stage.addActor(fin);
        } else {
            uk.setPosition(colWidth * 4f, rowHeight / 2f);
            stage.addActor(uk);
        }

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        prefs.putString("profile", profile.getText());
        profile.setText(prefs.getString("profile"));

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
        menuSkin.dispose();
    }
}
