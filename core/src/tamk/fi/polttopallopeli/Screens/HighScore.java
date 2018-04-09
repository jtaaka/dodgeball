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

    public static float scores[] = new float[10];
    public static Preferences prefs;

    private BitmapFont font;
    private GlyphLayout layout;
    private FreeTypeFontGenerator generator;
    private OrthographicCamera camera;

    private float WIDTH = Gdx.graphics.getWidth();
    private float HEIGHT = Gdx.graphics.getHeight();

    private float time;
    private String formatMin;
    private String formatSec;

    public HighScore (Dodgeball host) {
        this.host = host;
        batch = host.getBatch();

        font = new BitmapFont();
        layout = new GlyphLayout();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("digital-7-mono.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        parameter.borderWidth = 1;

        parameter.color = Color.WHITE;

        font = generator.generateFont(parameter);

        layout.setText(font, "NAME: 00:00");
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

        font.draw(batch, "HIGH SCORES", WIDTH / 2f - (layout.width / 2f),
                HEIGHT - layout.height * 2f);

        for (int i = 0; i < scores.length; i++) {
            if (scores[i] > scores[i]) {

                time = scores[i];
                int minutes = (int) (time / (1000 * 60));
                int seconds = (int) ((time / 1000) % 60);

                formatMin = String.format("%02d", minutes);
                formatSec = String.format("%02d", seconds);

                font.draw(batch, "Player: " + formatMin + ":" + formatSec, WIDTH / 2f - (layout.width / 2f),
                        HEIGHT - layout.height * 6f - (i * layout.height * 2f));
            } else {
                time = scores[i];
                int minutes = (int) (time / (1000 * 60));
                int seconds = (int) ((time / 1000) % 60);

                formatMin = String.format("%02d", minutes);
                formatSec = String.format("%02d", seconds);

                font.draw(batch, "Player: " + formatMin + ":" + formatSec, WIDTH / 2f - (layout.width / 2f),
                        HEIGHT - layout.height * 6f - (i * layout.height * 2f));
            }
        }

        batch.end();

        // For testing purposes
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            host.setScreen(new Menu(host));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            host.setScreen(new Menu(host));
        }
    }

    public static void setScore() {
        prefs = Gdx.app.getPreferences("HighScorePreferences");
        prefs.putFloat("score", scores[0]);
        prefs.putFloat("score", scores[1]);
        prefs.putFloat("score", scores[2]);
        prefs.putFloat("score", scores[3]);
        prefs.putFloat("score", scores[4]);
        prefs.putFloat("score", scores[5]);
        prefs.putFloat("score", scores[6]);
        prefs.putFloat("score", scores[7]);
        prefs.putFloat("score", scores[8]);
        prefs.putFloat("score", scores[9]);
        prefs.flush();
    }

    public static void getScore() {
        prefs = Gdx.app.getPreferences("HighScorePreferences");
        scores[0] = prefs.getFloat("score", 0);
        scores[1] = prefs.getFloat("score", 0);
        scores[2] = prefs.getFloat("score",0);
        scores[3] = prefs.getFloat("score",0);
        scores[4] = prefs.getFloat("score",0);
        scores[5] = prefs.getFloat("score",0);
        scores[6] = prefs.getFloat("score",0);
        scores[7] = prefs.getFloat("score",0);
        scores[8] = prefs.getFloat("score",0);
        scores[9] = prefs.getFloat("score",0);
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
