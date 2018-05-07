package tamk.fi.polttopallopeli.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import tamk.fi.polttopallopeli.Dodgeball;
import tamk.fi.polttopallopeli.ProfilePreferences;

/**
 * Highscore class for the best times in survival mode.
 *
 * @author  Juho Taakala <juho.taakala@cs.tamk.fi>
 *          Joni Alanko <joni.alanko@cs.tamk.fi>
 * @since   2018.0222
 * @version 1.0
 */
public class HighScore implements Screen  {

    /**
     * Defines "main class" as a host.
     */
    private Dodgeball host;

    /**
     * Defines SpriteBatch for high score screen.
     */
    private SpriteBatch batch;

    /**
     * Defines background texture for world.
     */
    private Texture background;

    /**
     * Defines stage for the level.
     */
    private Stage stage;

    /**
     * Defines a skin for button styles.
     */
    private Skin skin;

    /**
     * Defines back button.
     */
    private TextButton backButton;

    /**
     * Defines an array of highscore times.
     */
    public static long scores[] = new long[10];

    /**
     * Defines highscore preferences.
     */
    public static Preferences prefs;

    /**
     * Defines a font for the highscore times.
     */
    private BitmapFont font;

    /**
     * Defines a layout for the text.
     */
    private GlyphLayout layout;

    /**
     * Defines a generator for the font.
     */
    private FreeTypeFontGenerator generator;

    /**
     * Defines a camera for the highscore screen.
     */
    private OrthographicCamera camera;

    /**
     * Defines device's screen width.
     */
    private float WIDTH = Gdx.graphics.getWidth();

    /**
     * Defines device's screen height.
     */
    private float HEIGHT = Gdx.graphics.getHeight();

    /**
     * Defines device's screen column width to help position buttons.
     */
    private final int colWidth = Gdx.graphics.getWidth() / 12;

    /**
     * Defines device's screen row height to help position buttons.
     */
    private final int rowHeight = Gdx.graphics.getHeight() / 12;

    /**
     * Defines a time variable for high score time.
     */
    private long time;

    /**
     * Defines a profile variable for profile names.
     */
    private String profile;

    /**
     * Defines a variable for minutes formatting.
     */
    private String formatMin;

    /**
     * Defines a variable for seconds formatting.
     */
    private String formatSec;

    /**
     * Defines a variable for hundredths formatting.
     */
    private String formatHundredths;

    /**
     * Constructor for high score class.
     *
     * @param host "main class" host
     */
    public HighScore (Dodgeball host) {
        this.host = host;
        batch = host.getBatch();
        background = new Texture("highscorebg.png");

        stage = new Stage(new ScreenViewport(), batch);

        skin = new Skin(Gdx.files.internal("Holo-dark-xhdpi.json"));

        backButton = new TextButton(host.getLang().get("back"), skin, "default");
        backButton.setPosition(colWidth / 1.5f, rowHeight / 3f);

        font = new BitmapFont();
        layout = new GlyphLayout();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("droid-mono.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 36;
        parameter.color = Color.WHITE;
        parameter.borderWidth = 1;
        parameter.borderColor = Color.BLACK;

        font = generator.generateFont(parameter);
        layout.setText(font, "HIGH SCORES");

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

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
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        getScore();
        setScore();

        ProfilePreferences.getprofile();
        ProfilePreferences.setProfile();

        batch.begin();

        batch.setProjectionMatrix(camera.combined);
        camera.update();

        batch.draw(background, 0, 0, WIDTH, HEIGHT);

        font.draw(batch, host.getLang().get("highscore"), WIDTH / 2f - (layout.width / 2f),
                rowHeight * 12f - layout.height);

        drawScores();

        batch.end();

        stage.act();
        stage.draw();

        Gdx.input.setCatchBackKey(true);
    }

    /**
     * Draws scores on screen.
     */
    private void drawScores() {

        for (int i = 0; i < scores.length; i++) {

            time = scores[i];
            profile = ProfilePreferences.name[i];

            int minutes = (int) (time / (1000 * 60));
            int seconds = (int) ((time / 1000) % 60);
            int hundredths = (int)((time / 10) % 100);

            formatMin = String.format("%02d", minutes);
            formatSec = String.format("%02d", seconds);
            formatHundredths = String.format("%02d", hundredths);

            font.draw(batch, i + 1 + ".", colWidth * 3.5f,
                    rowHeight * 10f - (i * (rowHeight / 1.2f)));

            font.draw(batch, profile, colWidth * 4.5f,
                    rowHeight * 10f - (i * (rowHeight / 1.2f)));

            font.draw(batch,formatMin + ":" + formatSec + ":" + formatHundredths,
                    colWidth * 7f,
                    rowHeight * 10f - (i * (rowHeight / 1.2f)));
        }
    }

    /**
     * Sets high scores to profile preferences.
     */
    public static void setScore() {
        prefs = Gdx.app.getPreferences("HighScorePreferences");
        prefs.putLong("score0", scores[0]);
        prefs.putLong("score1", scores[1]);
        prefs.putLong("score2", scores[2]);
        prefs.putLong("score3", scores[3]);
        prefs.putLong("score4", scores[4]);
        prefs.putLong("score5", scores[5]);
        prefs.putLong("score6", scores[6]);
        prefs.putLong("score7", scores[7]);
        prefs.putLong("score8", scores[8]);
        prefs.putLong("score9", scores[9]);
        prefs.flush();
    }

    /**
     * Gets high scores from profile preferences.
     */
    public static void getScore() {
        prefs = Gdx.app.getPreferences("HighScorePreferences");
        scores[0] = prefs.getLong("score0", 0);
        scores[1] = prefs.getLong("score1", 0);
        scores[2] = prefs.getLong("score2",0);
        scores[3] = prefs.getLong("score3",0);
        scores[4] = prefs.getLong("score4",0);
        scores[5] = prefs.getLong("score5",0);
        scores[6] = prefs.getLong("score6",0);
        scores[7] = prefs.getLong("score7",0);
        scores[8] = prefs.getLong("score8",0);
        scores[9] = prefs.getLong("score9",0);
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
        generator.dispose();
        background.dispose();
        stage.dispose();
        skin.dispose();
    }
}
