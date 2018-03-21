package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.TimeUtils;

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

    public GameTimer(SpriteBatch batch) {
        this.batch = batch;
        font = new BitmapFont();
        layout = new GlyphLayout();
        freeze = false;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Dodgeball.WINDOW_WIDTH, Dodgeball.WINDOW_HEIGHT);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("digital-7.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 70;
        parameter.borderWidth = 1;
        parameter.color = Color.BLACK;
        font = generator.generateFont(parameter);

        layout.setText(font, "00:00");
    }

    public void setFreeze() {
        freeze = true;
    }

    public float getElapsedTime() {
        elapsed = (TimeUtils.nanoTime() - startTime) / nanosPerMilli;

        // 1000 = 1 sec
        return elapsed;
    }

    public void survivalModeTimer() {

        if (!freeze) {
            elapsed = (TimeUtils.nanoTime() - startTime) / nanosPerMilli;
        }

        int minutes = (int) (elapsed / (1000 * 60));
        int seconds = (int) ((elapsed / 1000) % 60);

        String formatMin = String.format("%02d", minutes);
        String formatSec = String.format("%02d", seconds);

        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        camera.update();
        font.draw(batch, formatMin + ":" + formatSec, Dodgeball.WINDOW_WIDTH - layout.width,
                Dodgeball.WINDOW_HEIGHT - layout.height / 2);
        batch.end();
    }
}
