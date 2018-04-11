package tamk.fi.polttopallopeli.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import tamk.fi.polttopallopeli.Dodgeball;

public class HighScore implements Screen  {
    private Dodgeball host;
    private SpriteBatch batch;

    public static long scores[] = new long[10];
    public static Preferences prefs;

    private BitmapFont font;
    private GlyphLayout layout;
    private GlyphLayout layout2;
    private FreeTypeFontGenerator generator;
    private OrthographicCamera camera;

    private float WIDTH = Gdx.graphics.getWidth();
    private float HEIGHT = Gdx.graphics.getHeight();

    private long time;
    private String formatMin;
    private String formatSec;
    private String formatHundredths;

    public HighScore (Dodgeball host) {
        this.host = host;
        batch = host.getBatch();

        font = new BitmapFont();
        layout = new GlyphLayout();
        layout2 = new GlyphLayout();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("droid-mono.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        parameter.borderWidth = 1;
        parameter.color = Color.WHITE;

        font = generator.generateFont(parameter);

        layout.setText(font, "NAME: 00:00:00");
        layout2.setText(font, "HIGH SCORES");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        camera.update();

        getScore();
        setScore();

        batch.begin();

        font.draw(batch, "HIGH SCORES", WIDTH / 2f - (layout2.width / 2f),
                HEIGHT - layout2.height * 2f);

        for (int i = 0; i < scores.length; i++) {

            time = scores[i];
            int minutes = (int) (time / (1000 * 60));
            int seconds = (int) ((time / 1000) % 60);
            int hundredths = (int)((time / 10) % 100);

            formatMin = String.format("%02d", minutes);
            formatSec = String.format("%02d", seconds);
            formatHundredths = String.format("%02d", hundredths);

            font.draw(batch, "Name: " + formatMin + ":" + formatSec + ":" + formatHundredths,
                    WIDTH / 2f - (layout.width / 2f),
                    HEIGHT - layout.height * 6f - (i * layout.height * 2f));
        }

        batch.end();

        // For testing purposes
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            host.setScreen(new Menu(host));
        }

        if (Gdx.input.isTouched()) {
            host.setScreen(new Menu(host));
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
    }
}
