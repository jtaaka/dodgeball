package tamk.fi.polttopallopeli.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import tamk.fi.polttopallopeli.Dodgeball;
import tamk.fi.polttopallopeli.Screens.Menu;
import tamk.fi.polttopallopeli.SettingsPreferences;

public class Settings implements Screen {
    private SpriteBatch batch;
    private Dodgeball host;
    private Texture background;

    private Skin skin;
    private TextField profile;

    private Slider sliderZPositive;
    private Slider sliderZNegative;
    private Slider sliderXPositive;
    private Slider sliderXNegative;
    private Container sliderLeft;
    private Container sliderDown;
    //private Texture sliderBackgroundTex, sliderKnobTex, sliderBackgroundTexVert;*/
    private Stage stage;

    final int colWidth = Gdx.graphics.getWidth() / 12;
    final int rowHeight = Gdx.graphics.getHeight() / 12;
    final float WIDTH = Gdx.graphics.getWidth();
    final float HEIGHT = Gdx.graphics.getHeight();

    public Settings (Dodgeball host) {
        this.host = host;
        batch = host.getBatch();
        background = new Texture("levelsbg.png");

        SettingsPreferences.getSettings();

        stage = new Stage(new ScreenViewport(), batch);

        skin = new Skin(Gdx.files.internal("Holo-dark-xhdpi.json"));
        profile = new TextField("name", skin, "default");
        profile.setSize(colWidth * 2f, rowHeight);
        profile.setPosition(colWidth * 10f, rowHeight);

        sliderZPositive = new Slider(1f, 5f, 0.5f, true, skin, "up-vertical");
        sliderZPositive.setValue(SettingsPreferences.prefs.getFloat("calibrationZPositive"));
        sliderZPositive.setPosition(colWidth * 2f, colWidth * 4f);

        sliderZNegative = new Slider(1f, 5f, 0.5f, true, skin, "up-vertical");
        sliderZNegative.setValue(SettingsPreferences.prefs.getFloat("calibrationZNegative"));

        sliderXPositive = new Slider(1f, 5f, 0.5f, false, skin, "left-horizontal");
        sliderXPositive.setValue(SettingsPreferences.prefs.getFloat("calibrationXPositive"));
        sliderXPositive.setPosition(colWidth * 2.75f, colWidth * 3.3f);

        sliderXNegative = new Slider(1f, 5f, 0.5f, false, skin, "left-horizontal");
        sliderXNegative.setValue(SettingsPreferences.prefs.getFloat("calibrationXNegative"));

        sliderLeft = new Container(sliderXNegative);
        sliderLeft.setTransform(true);
        sliderLeft.setRotation(-180f);
        sliderLeft.setPosition(colWidth * 1.2f, colWidth * 3.6f);

        sliderDown = new Container(sliderZNegative);
        sliderDown.setTransform(true);
        sliderDown.setRotation(-180f);
        sliderDown.setPosition(colWidth * 2.3f, colWidth * 2.5f);

        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

        sliderZPositive.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(getClass().getSimpleName(), "sliderZup changed to: " + sliderZPositive.getValue());
                SettingsPreferences.prefs.putFloat("calibrationZPositive", sliderZPositive.getValue());
                SettingsPreferences.prefs.flush();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        sliderDown.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(getClass().getSimpleName(), "sliderZdown changed to: " + sliderZNegative.getValue());
                SettingsPreferences.prefs.putFloat("calibrationZNegative", sliderZNegative.getValue());
                SettingsPreferences.prefs.flush();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });


        sliderXPositive.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(getClass().getSimpleName(), "sliderXright changed to: " + sliderXPositive.getValue());
                SettingsPreferences.prefs.putFloat("calibrationXPositive", sliderXPositive.getValue());
                SettingsPreferences.prefs.flush();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        sliderLeft.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(getClass().getSimpleName(), "sliderXleft changed to: " + sliderXNegative.getValue());
                SettingsPreferences.prefs.putFloat("calibrationXNegative", sliderXNegative.getValue());
                SettingsPreferences.prefs.flush();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        stage.addActor(profile);
        stage.addActor(sliderZPositive);
        stage.addActor(sliderXPositive);
        stage.addActor(sliderLeft);
        stage.addActor(sliderDown);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, WIDTH, HEIGHT);
        batch.end();

        // For testing purposes
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            host.setScreen(new Menu(host));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            host.setScreen(new Menu(host));
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
        background.dispose();
        skin.dispose();
    }
}
