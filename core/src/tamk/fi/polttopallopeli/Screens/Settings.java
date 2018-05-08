package tamk.fi.polttopallopeli.Screens;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import tamk.fi.polttopallopeli.Dodgeball;
import tamk.fi.polttopallopeli.SettingsPreferences;

/**
 * A class for the settings screen.
 *
 * @author  Juho Taakala <juho.taakala@cs.tamk.fi>
 *          Joni Alanko <joni.alanko@cs.tamk.fi>
 * @since   2018.0222
 * @version 1.0
 */
public class Settings implements Screen {

    /**
     * Defines SpriteBatch for the settings screen.
     */
    private SpriteBatch batch;

    /**
     * Defines "main class" as a host.
     */
    private Dodgeball host;

    /**
     * Defines a background texture for the settings screen.
     */
    private Texture background;

    /**
     * Defines a skin for the settings stuff.
     */
    private Skin skin;

    /**
     * Defines a up text label.
     */
    private Label up;

    /**
     * Defines a down text label.
     */
    private Label down;

    /**
     * Defines a right text label.
     */
    private Label right;

    /**
     * Defines a left text label.
     */
    private Label left;

    /**
     * Defines a settings text label.
     */
    private Label settings;

    /**
     * Defines a up calibration button.
     */
    private TextButton upCalibration;

    /**
     * Defines a down calibration button.
     */
    private TextButton downCalibration;

    /**
     * Defines a right calibration button.
     */
    private TextButton rightCalibration;

    /**
     * Defines a left calibration button.
     */
    private TextButton leftCalibration;

    /**
     * Defines a back button.
     */
    private TextButton backButton;

    /**
     * Defines a Z-axis slider.
     */
    private Slider sliderZPositive;

    /**
     * Defines a negative Z-axis slider.
     */
    private Slider sliderZNegative;

    /**
     * Defines a X-axis slider.
     */
    private Slider sliderXPositive;

    /**
     * Defines a negative X-axis slider.
     */
    private Slider sliderXNegative;

    /**
     * Defines a container for the left slider.
     */
    private Container sliderLeft;

    /**
     * Defines a container for the down slider.
     */
    private Container sliderDown;

    /**
     * Defines a stage for the settings screen.
     */
    private Stage stage;

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
     * Constructor for settings screen.
     *
     * @param host "main class" host.
     */
    public Settings (Dodgeball host) {
        this.host = host;
        batch = host.getBatch();
        background = new Texture("levelsbg.png");

        SettingsPreferences.getSettings();

        stage = new Stage(new ScreenViewport(), batch);

        skin = new Skin(Gdx.files.internal("Holo-dark-xhdpi.json"));

        settings = new Label(host.getLang().get("calibration"), skin, "default");
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
        sliderLeft.rotateBy(180f);
        sliderLeft.top().right();
        sliderLeft.setPosition(colWidth * 2f, rowHeight * 5f);

        sliderDown = new Container(sliderZNegative);
        sliderDown.setTransform(true);
        sliderDown.rotateBy(180f);
        sliderDown.right();
        sliderDown.setPosition(colWidth * 3.3f, rowHeight * 4f);

        upCalibration = new TextButton("" + sliderZPositive.getValue(), skin, "default");
        upCalibration.setPosition(colWidth * 10f, rowHeight * 7.5f);

        rightCalibration = new TextButton("" + sliderXPositive.getValue(), skin, "default");
        rightCalibration.setPosition(colWidth * 10f, rowHeight * 6f);

        downCalibration = new TextButton("" + sliderZNegative.getValue(), skin, "default");
        downCalibration.setPosition(colWidth * 10f, rowHeight * 4.5f);

        leftCalibration = new TextButton("" + sliderXNegative.getValue(), skin, "default");
        leftCalibration.setPosition(colWidth * 10f, rowHeight * 3f);

        up = new Label(host.getLang().get("up"), skin, "default");
        up.setPosition(colWidth * 6.5f, rowHeight * 7.5f);

        right = new Label(host.getLang().get("right"), skin, "default");
        right.setPosition(colWidth * 6.5f, rowHeight * 6f);

        down = new Label(host.getLang().get("down"), skin, "default");
        down.setPosition(colWidth * 6.5f, rowHeight * 4.5f);

        left = new Label(host.getLang().get("left"), skin, "default");
        left.setPosition(colWidth * 6.5f, rowHeight * 3f);

        backButton = new TextButton(host.getLang().get("back"), skin, "default");
        backButton.setPosition(colWidth / 2, rowHeight / 2f);

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

        backButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                host.setScreen(new Menu(host));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        stage.addActor(backButton);
        stage.addActor(settings);
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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, WIDTH, HEIGHT);
        batch.end();

        upCalibration.setText("" + sliderZPositive.getValue());
        downCalibration.setText("" + sliderZNegative.getValue());
        rightCalibration.setText("" + sliderXPositive.getValue());
        leftCalibration.setText("" + sliderXNegative.getValue());

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
