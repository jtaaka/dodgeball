package tamk.fi.polttopallopeli.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import tamk.fi.polttopallopeli.Dodgeball;
import tamk.fi.polttopallopeli.SettingsPreferences;

public class Settings implements Screen {
    private SpriteBatch batch;
    private Dodgeball host;
    private Texture background;
    private Skin skin;

    private TextField profile;
    private Label profileName;

    private Label up;
    private Label down;
    private Label right;
    private Label left;
    private Label settings;

    private TextButton upCalibration;
    private TextButton downCalibration;
    private TextButton rightCalibration;
    private TextButton leftCalibration;

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

        settings = new Label("Calibration Settings", skin, "default");
        settings.setPosition(WIDTH / 2 - settings.getWidth() / 2, HEIGHT - settings.getHeight());

        sliderZPositive = new Slider(1f, 5f, 0.5f, true, skin, "up-vertical");
        sliderZPositive.setValue(SettingsPreferences.prefs.getFloat("calibrationZPositive"));
        sliderZPositive.setPosition(colWidth * 3.3f, rowHeight * 6f);

        sliderZNegative = new Slider(1f, 5f, 0.5f, true, skin, "up-vertical");
        sliderZNegative.setValue(SettingsPreferences.prefs.getFloat("calibrationZNegative"));

        sliderXPositive = new Slider(1f, 5f, 0.5f, false, skin, "left-horizontal");
        sliderXPositive.setValue(SettingsPreferences.prefs.getFloat("calibrationXPositive"));
        sliderXPositive.setPosition(colWidth * 4f, rowHeight * 5f);

        sliderXNegative = new Slider(1f, 5f, 0.5f, false, skin, "left-horizontal");
        sliderXNegative.setValue(SettingsPreferences.prefs.getFloat("calibrationXNegative"));

        sliderLeft = new Container(sliderXNegative);
        sliderLeft.setTransform(true);
        sliderLeft.setRotation(-180f);
        sliderLeft.setPosition(colWidth * 2.55f, rowHeight * 5.47f);

        sliderDown = new Container(sliderZNegative);
        sliderDown.setTransform(true);
        sliderDown.setRotation(-180f);
        sliderDown.setPosition(colWidth * 3.60f, colWidth * 2.4f);

        profile = new TextField("", skin, "default");
        profile.setMaxLength(10);
        profile.setMessageText("Type name here");
        profile.setSize(colWidth * 3f, rowHeight);
        profile.setPosition(colWidth * 8f, rowHeight);

        upCalibration = new TextButton("" + sliderZPositive.getValue(), skin, "default");
        upCalibration.setPosition(colWidth * 10f, rowHeight * 8f);

        rightCalibration = new TextButton("" + sliderXPositive.getValue(), skin, "default");
        rightCalibration.setPosition(colWidth * 10f, rowHeight * 6.5f);

        downCalibration = new TextButton("" + sliderZNegative.getValue(), skin, "default");
        downCalibration.setPosition(colWidth * 10f, rowHeight * 5f);

        leftCalibration = new TextButton("" + sliderXNegative.getValue(), skin, "default");
        leftCalibration.setPosition(colWidth * 10f, rowHeight * 3.5f);

        up = new Label("Up Calibration", skin, "default");
        up.setPosition(colWidth * 6.5f, rowHeight * 8f);

        right = new Label("Right Calibration", skin, "default");
        right.setPosition(colWidth * 6.5f, rowHeight * 6.5f);

        down = new Label("Down Calibration", skin, "default");
        down.setPosition(colWidth * 6.5f, rowHeight * 5f);

        left = new Label("Left Calibration", skin, "default");
        left.setPosition(colWidth * 6.5f, rowHeight * 3.5f);

        profileName = new Label("Profile", skin, "default");
        profileName.setPosition(colWidth * 6.5f, rowHeight * 0.9f);

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
                upCalibration.setText("" + sliderZPositive.getValue());
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
                downCalibration.setText("" + sliderZNegative.getValue());
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
                rightCalibration.setText("" + sliderXPositive.getValue());
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
                leftCalibration.setText("" + sliderXNegative.getValue());
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        stage.addActor(settings);
        stage.addActor(profile);
        stage.addActor(profileName);
        stage.addActor(sliderZPositive);
        stage.addActor(sliderXPositive);
        stage.addActor(sliderLeft);
        stage.addActor(sliderDown);
        stage.addActor(upCalibration);
        stage.addActor(rightCalibration);
        stage.addActor(downCalibration);
        stage.addActor(leftCalibration);
        stage.addActor(up);
        stage.addActor(right);
        stage.addActor(down);
        stage.addActor(left);
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
