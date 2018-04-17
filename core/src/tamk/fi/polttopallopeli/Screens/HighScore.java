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

public class HighScore implements Screen  {
    private Dodgeball host;
    private SpriteBatch batch;
    private Texture background;

    private Stage stage;
    private Skin skin;
    private TextButton backButton;

    public static long scores[] = new long[10];
    public static Preferences prefs;

    private BitmapFont font;
    private GlyphLayout layout;
    private FreeTypeFontGenerator generator;
    private OrthographicCamera camera;

    private float WIDTH = Gdx.graphics.getWidth();
    private float HEIGHT = Gdx.graphics.getHeight();
    final int colWidth = Gdx.graphics.getWidth() / 12;
    final int rowHeight = Gdx.graphics.getHeight() / 12;

    private long time;
    private String profile;
    private String formatMin;
    private String formatSec;
    private String formatHundredths;

    public HighScore (Dodgeball host) {
        this.host = host;
        batch = host.getBatch();
        background = new Texture("highscorebg.png");

        stage = new Stage(new ScreenViewport(), batch);

        skin = new Skin(Gdx.files.internal("Holo-dark-xhdpi.json"));

        backButton = new TextButton("< Back", skin, "default");
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

        font.draw(batch, "HIGH SCORES", WIDTH / 2f - (layout.width / 2f),
                rowHeight * 12f - layout.height);

        drawScores();

        batch.end();

        stage.act();
        stage.draw();

        Gdx.input.setCatchBackKey(true);
    }

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
