package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Handles game timer.
 *
 * @author  Joni Alanko <joni.alanko@cs.tamk.fi>
 *          Juho Taakala <juho.taakala@cs.tamk.fi>
 * @since   2018.0222
 * @version 1.0
 */
public class GameTimer {

    /**
     * Defines font for the game timer.
     */
    private BitmapFont font;

    /**
     * Defines layout for the game timer.
     */
    private GlyphLayout layout;

    /**
     * Defines generator for the font.
     */
    private FreeTypeFontGenerator generator;

    /**
     * Defines SpriteBatch for the game timer.
     */
    private SpriteBatch batch;

    /**
     * Defines stage for the count down timer actors.
     */
    private Stage stage;

    /**
     * Defines a camera.
     */
    private OrthographicCamera camera;

    /**
     * Defines a countdown actor.
     */
    private Image countDown1;

    /**
     * Defines a countdown actor.
     */
    private Image countDown2;

    /**
     * Defines a countdown actor.
     */
    private Image countDown3;

    /**
     * Defines a countdown actor.
     */
    private Image go;

    /**
     * Defines start time in nanoseconds.
     */
    private long startTime = TimeUtils.nanoTime();

    /**
     * Defines conversion from nanoseconds to milliseconds.
     */
    private long nanosPerMilli = 1000000;

    /**
     * Defines elapsed time.
     */
    private long elapsed;

    /**
     * Defines if the game timer is frozen.
     */
    private boolean freeze;

    /**
     * Defines device's screen width.
     */
    private final float WIDTH = Gdx.graphics.getWidth();

    /**
     * Defines device's screen height.
     */
    private final float HEIGHT = Gdx.graphics.getHeight();

    /**
     * Overloads constructor.
     *
     * @param batch host sprite batch.
     * @param whiteTimer indicates if outlined timer is used.
     * @param stage is same as in game.
     */
    public GameTimer(SpriteBatch batch, boolean whiteTimer, Stage stage) {
        this.batch = batch;
        this.stage = stage;
        font = new BitmapFont();
        layout = new GlyphLayout();
        freeze = true;

        countDown1 = new Image(new Texture("countdown1.png"));
        countDown1.setPosition(Gdx.graphics.getWidth() / 2 - countDown1.getWidth(),
                Gdx.graphics.getHeight() / 2 - countDown1.getHeight() / 2);

        countDown2 = new Image(new Texture("countdown2.png"));
        countDown2.setPosition(Gdx.graphics.getWidth() / 2 - countDown2.getWidth(),
                Gdx.graphics.getHeight() / 2 - countDown2.getHeight() / 2);

        countDown3 = new Image(new Texture("countdown3.png"));
        countDown3.setPosition(Gdx.graphics.getWidth() / 2 - countDown3.getWidth(),
                Gdx.graphics.getHeight() / 2 - countDown3.getHeight() / 2);

        go = new Image(new Texture("countdownGo.png"));
        go.setPosition(Gdx.graphics.getWidth() / 2 - go.getWidth(),
                Gdx.graphics.getHeight() / 2 - go.getHeight() / 2);

        Gdx.input.setInputProcessor(stage);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("digital-7-mono.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 70;
        parameter.borderWidth = 1;

        if (whiteTimer) {
            parameter.color = Color.BLACK;
            parameter.borderColor = Color.WHITE;
        } else {
            parameter.color = Color.BLACK;
        }

        font = generator.generateFont(parameter);

        layout.setText(font, "00:00");

        countDown3.addAction(Actions.scaleBy(1.2f, 1.2f, 0.3f));

        countDown2.addAction(Actions.scaleBy(1.2f, 1.2f, 0.3f));

        countDown1.addAction(Actions.scaleBy(1.2f, 1.2f, 0.3f));

        go.addAction(Actions.scaleBy(1f, 1f, 0.3f));
    }

    /**
     * Freezes timer.
     */
    public void setFreeze() {
        freeze = true;
    }

    /**
     * Gets time result on survival mode.
     *
     * @return elapsed time.
     */
    public long getHighScoreTime() {
        if (!freeze) {
            elapsed = (TimeUtils.nanoTime() - startTime) / nanosPerMilli;
        }

        return elapsed;
    }

    /**
     * Gets elapsed time and converts it to seconds.
     *
     * @return time in seconds.
     */
    public long getElapsedTime() {
        return (TimeUtils.nanoTime() - startTime) / nanosPerMilli / 1000;
    }

    /**
     * Count down timer for the game start.
     *
     * @param pauseDelay time in pause mode.
     */
    public void countDownTimer(long pauseDelay) {
        freeze = true;

        if (getElapsedTime() - pauseDelay >= 1f) {
            stage.addActor(countDown3);
        }

        if (getElapsedTime() - pauseDelay >= 2f) {
            countDown3.remove();
            stage.addActor(countDown2);
        }

        if (getElapsedTime() - pauseDelay >= 3f) {
            countDown2.remove();
            stage.addActor(countDown1);
        }

        if (getElapsedTime() - pauseDelay >= 4f) {
            countDown1.remove();
            stage.addActor(go);
        }

        if (getElapsedTime() - pauseDelay >= 5f) {
            go.remove();
            freeze = false;
        }
    }

    /**
     * Handles timer for survival mode counting up.
     *
     * @param pauseDelay how long game was paused.
     */
    public void survivalModeTimer(long pauseDelay) {

        if (!freeze) {
            elapsed = (TimeUtils.nanoTime() - startTime - pauseDelay) / nanosPerMilli - 5000;
        }

        int minutes = (int) (elapsed / (1000 * 60));
        int seconds = (int) ((elapsed / 1000) % 60);

        String formatMin = String.format("%02d", minutes);
        String formatSec = String.format("%02d", seconds);

        //fps = Gdx.graphics.getFramesPerSecond();

        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        camera.update();

        font.draw(batch, formatMin + ":" + formatSec, WIDTH - (layout.width + 50f),
                HEIGHT - layout.height / 2);

        batch.end();
    }

    /**
     * Handles timer for levels counting down and stopping at 0.
     *
     * @param timeLimit Sets the timer to this.
     * @param pauseDelay How long game was paused.
     */
    public void levelModeTimer(long timeLimit, long pauseDelay) {
        timeLimit = (timeLimit + 1) * 1000;

        if (!freeze) {
            elapsed = (timeLimit * nanosPerMilli + startTime - TimeUtils.nanoTime() + pauseDelay) / nanosPerMilli;
        }

        int minutes = (int) (elapsed / (1000 * 60));
        int seconds = (int) ((elapsed / 1000) % 60);

        String formatMin = String.format("%02d", minutes);
        String formatSec = String.format("%02d", seconds);

        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        camera.update();

        if (!freeze) {
            font.draw(batch, formatMin + ":" + formatSec, WIDTH - (layout.width + 50f),
                    HEIGHT - layout.height / 2);
        }

        batch.end();
    }

    public void dispose() {
        generator.dispose();
        font.dispose();
        stage.dispose();
    }
}
