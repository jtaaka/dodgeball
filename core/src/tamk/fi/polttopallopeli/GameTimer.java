package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.TimeUtils;

import tamk.fi.polttopallopeli.CampaignLevels.LevelTemplate;

public class GameTimer {
    private BitmapFont font;
    private GlyphLayout layout;
    private FreeTypeFontGenerator generator;
    private SpriteBatch batch;
    private OrthographicCamera camera;

    private long startTime = TimeUtils.nanoTime();
    private long nanosPerMilli = 1000000;
    private long elapsed;
    private boolean freeze;
    private float x;
    private float y;

    private BitmapFont fpsFont;
    private float fps;

    public GameTimer(SpriteBatch batch, boolean whiteTimer) {
        this.batch = batch;
        font = new BitmapFont();
        layout = new GlyphLayout();
        freeze = false;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Dodgeball.WINDOW_WIDTH, Dodgeball.WINDOW_HEIGHT);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("digital-7-mono.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 70;
        parameter.borderWidth = 1;

        if (whiteTimer) {
            parameter.color = Color.WHITE;
        } else {
            parameter.color = Color.BLACK;
        }
        font = generator.generateFont(parameter);

        layout.setText(font, "00:00");

        // fps testaukseen
        fpsFont = new BitmapFont();
        FreeTypeFontGenerator.FreeTypeFontParameter p2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p2.size = 20;
        p2.color = Color.BLACK;
        fpsFont = generator.generateFont(p2);
    }

    public void setFreeze() {
        freeze = true;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getElapsedTime() {

        // 1000 = 1 sec
        return (TimeUtils.nanoTime() - startTime) / nanosPerMilli / 1000;
    }

    public void survivalModeTimer() {

        if (!freeze) {
            elapsed = (TimeUtils.nanoTime() - startTime) / nanosPerMilli;
        }

        int minutes = (int) (elapsed / (1000 * 60));
        int seconds = (int) ((elapsed / 1000) % 60);

        String formatMin = String.format("%02d", minutes);
        String formatSec = String.format("%02d", seconds);

        fps = Gdx.graphics.getFramesPerSecond();

        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        camera.update();
        font.draw(batch, formatMin + ":" + formatSec, Dodgeball.WINDOW_WIDTH - (layout.width + 50f),
                Dodgeball.WINDOW_HEIGHT - layout.height / 2);

        fpsFont.draw(batch, (int)fps + " fps", Dodgeball.WINDOW_WIDTH / 400f,
                Dodgeball.WINDOW_HEIGHT - 100f);

        // Pelaajan liikkeen keskipiste x ja y
        if (x != 0 && y != 0) {
            fpsFont.draw(batch, "y: " + y, Dodgeball.WINDOW_WIDTH / 400f, Dodgeball.WINDOW_HEIGHT -200f);
            fpsFont.draw(batch, "x: " + x, Dodgeball.WINDOW_WIDTH / 400f, Dodgeball.WINDOW_HEIGHT -250f);
        }

        batch.end();
    }

    public void levelModeTimer(long timeLimit) {
        timeLimit = (timeLimit + 1) * 1000;


        if (!freeze) {
            elapsed = (timeLimit * nanosPerMilli + startTime - TimeUtils.nanoTime()) / nanosPerMilli;
        }

        int minutes = (int) (elapsed / (1000 * 60));
        int seconds = (int) ((elapsed / 1000) % 60);

        String formatMin = String.format("%02d", minutes);
        String formatSec = String.format("%02d", seconds);

        fps = Gdx.graphics.getFramesPerSecond();

        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        camera.update();
        font.draw(batch, formatMin + ":" + formatSec, Dodgeball.WINDOW_WIDTH - (layout.width + 50f),
                Dodgeball.WINDOW_HEIGHT - layout.height / 2);

        fpsFont.draw(batch, (int)fps + " fps", Dodgeball.WINDOW_WIDTH / 400f,
                Dodgeball.WINDOW_HEIGHT - 100f);

        // Pelaajan liikkeen keskipiste x ja y
        if (x != 0 && y != 0) {
            fpsFont.draw(batch, "y: " + y, Dodgeball.WINDOW_WIDTH / 400f, Dodgeball.WINDOW_HEIGHT -200f);
            fpsFont.draw(batch, "x: " + x, Dodgeball.WINDOW_WIDTH / 400f, Dodgeball.WINDOW_HEIGHT -250f);
        }

        batch.end();
    }

    public void dispose() {
        generator.dispose();
    }
}
