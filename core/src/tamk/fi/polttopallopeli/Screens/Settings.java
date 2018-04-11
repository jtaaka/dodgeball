package tamk.fi.polttopallopeli.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import tamk.fi.polttopallopeli.Dodgeball;
import tamk.fi.polttopallopeli.Screens.Menu;
import tamk.fi.polttopallopeli.SettingsPreferences;

public class Settings implements Screen {
    private SpriteBatch batch;
    private Dodgeball host;
    private Texture background;

    Slider sliderZPositive;
    Slider sliderZNegative;
    Slider sliderXPositive;
    Slider sliderXNegative;
    private Texture sliderBackgroundTex, sliderKnobTex, sliderBackgroundTexVert;
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
        Gdx.input.setInputProcessor(stage);

        sliderBackgroundTex = new Texture(Gdx.files.internal("sliderBackground.png"));
        sliderBackgroundTexVert = new Texture(Gdx.files.internal("sliderBackgroundVert.png"));
        sliderKnobTex = new Texture(Gdx.files.internal("sliderButton.png"));
        Slider.SliderStyle ssvert = new Slider.SliderStyle();
        ssvert.background = new TextureRegionDrawable(new TextureRegion(sliderBackgroundTexVert));
        ssvert.knob = new TextureRegionDrawable(new TextureRegion(sliderKnobTex));

        sliderZPositive = new Slider(1f, 5f, 0.5f, true, ssvert);
        sliderZPositive.setValue(SettingsPreferences.prefs.getFloat("calibrationZPositive"));

        sliderZNegative = new Slider(1f, 5f, 0.5f, true, ssvert);
        sliderZNegative.setValue(SettingsPreferences.prefs.getFloat("calibrationZNegative"));

        Slider.SliderStyle ss = new Slider.SliderStyle();
        ss.background = new TextureRegionDrawable(new TextureRegion(sliderBackgroundTex));
        ss.knob = new TextureRegionDrawable(new TextureRegion(sliderKnobTex));
        sliderXPositive = new Slider(1f, 5f, 0.5f, false, ss);
        sliderXPositive.setValue(SettingsPreferences.prefs.getFloat("calibrationXPositive"));

        sliderXNegative = new Slider(1f, 5f, 0.5f, false, ss);
        sliderXNegative.setValue(SettingsPreferences.prefs.getFloat("calibrationXNegative"));

        sliderZPositive.setPosition(stage.getWidth() / 2f - sliderZPositive.getWidth() / 2f, stage.getHeight() * 0.66f - sliderZPositive.getHeight() / 2f);
        sliderZNegative.setPosition(stage.getWidth() / 2f - sliderZNegative.getWidth() / 2f, stage.getHeight() / 3f - sliderZNegative.getHeight() / 2f);
        sliderXPositive.setPosition(stage.getWidth() * 0.66f - sliderXPositive.getWidth() / 2f, stage.getHeight() / 2f - sliderXPositive.getHeight() / 2f);
        sliderXNegative.setPosition(stage.getWidth() / 3f - sliderXNegative.getWidth() / 2f, stage.getHeight() / 2f - sliderXNegative.getHeight() / 2f);

        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void show() {

        // Slider listener
        sliderZPositive.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(getClass().getSimpleName(), "slider changed to: " + sliderZPositive.getValue());
                SettingsPreferences.prefs.putFloat("calibrationZPositive", sliderZPositive.getValue());
                SettingsPreferences.prefs.flush();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        sliderZNegative.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(getClass().getSimpleName(), "slider changed to: " + sliderZNegative.getValue());
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
                Gdx.app.log(getClass().getSimpleName(), "slider changed to: " + sliderXPositive.getValue());
                SettingsPreferences.prefs.putFloat("calibrationXPositive", sliderXPositive.getValue());
                SettingsPreferences.prefs.flush();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        sliderXNegative.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(getClass().getSimpleName(), "slider changed to: " + sliderXNegative.getValue());
                SettingsPreferences.prefs.putFloat("calibrationXNegative", sliderXNegative.getValue());
                SettingsPreferences.prefs.flush();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        stage.addActor(sliderZPositive);
        stage.addActor(sliderZNegative);
        stage.addActor(sliderXPositive);
        stage.addActor(sliderXNegative);
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
        sliderBackgroundTex.dispose();
        sliderKnobTex.dispose();
        sliderBackgroundTexVert.dispose();
        background.dispose();
    }
}
