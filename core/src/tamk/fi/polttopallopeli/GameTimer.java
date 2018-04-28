package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Game;
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
import com.badlogic.gdx.scenes.scene2d.actions.ScaleByAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.SizeToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;

import tamk.fi.polttopallopeli.CampaignLevels.LevelTemplate;
import tamk.fi.polttopallopeli.Screens.Menu;

public class GameTimer {
    private BitmapFont font;
    private GlyphLayout layout;
    private FreeTypeFontGenerator generator;
    private SpriteBatch batch;
    private Stage stage;
    private OrthographicCamera camera;

    private Image countDown1;
    private Image countDown2;
    private Image countDown3;
    private Image go;

    private long startTime = TimeUtils.nanoTime();
    private long nanosPerMilli = 1000000;
    private long elapsed;
    private boolean freeze;
    private float x;
    private float y;

    final float WIDTH = Gdx.graphics.getWidth();
    final float HEIGHT = Gdx.graphics.getHeight();

    //private BitmapFont fpsFont;
    //private float fps;

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

        // fps testaukseen
        /*fpsFont = new BitmapFont();
        FreeTypeFontGenerator.FreeTypeFontParameter p2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p2.size = 20;
        p2.color = Color.BLACK;
        fpsFont = generator.generateFont(p2);*/

        countDown3.addAction(Actions.scaleBy(1.2f, 1.2f, 0.3f));

        countDown2.addAction(Actions.scaleBy(1.2f, 1.2f, 0.3f));

        countDown1.addAction(Actions.scaleBy(1.2f, 1.2f, 0.3f));

        go.addAction(Actions.scaleBy(1f, 1f, 0.3f));
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

    public long getHighScoreTime() {
        if (!freeze) {
            elapsed = (TimeUtils.nanoTime() - startTime) / nanosPerMilli;
        }

        return elapsed;
    }

    public long getElapsedTime() {
        return (TimeUtils.nanoTime() - startTime) / nanosPerMilli / 1000;
    }

    public void countDownTimer() {
        freeze = true;

        if (getElapsedTime() >= 1f) {
            stage.addActor(countDown3);
        }

        if (getElapsedTime() >= 2f) {
            countDown3.remove();
            stage.addActor(countDown2);
        }

        if (getElapsedTime() >= 3f) {
            countDown2.remove();
            stage.addActor(countDown1);
        }

        if (getElapsedTime() >= 4f) {
            countDown1.remove();
            stage.addActor(go);
        }

        if (getElapsedTime() >= 5f) {
            go.remove();
            freeze = false;
        }
    }

    public void survivalModeTimer() {

        if (!freeze) {
            elapsed = (TimeUtils.nanoTime() - startTime) / nanosPerMilli - 5000;
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


        /*fpsFont.draw(batch, (int)fps + " fps", Dodgeball.WINDOW_WIDTH / 400f,
                Dodgeball.WINDOW_HEIGHT - 100f);

        // Pelaajan liikkeen keskipiste x ja y
        if (x != 0 && y != 0) {
            fpsFont.draw(batch, "y: " + y, Dodgeball.WINDOW_WIDTH / 400f, Dodgeball.WINDOW_HEIGHT -200f);
            fpsFont.draw(batch, "x: " + x, Dodgeball.WINDOW_WIDTH / 400f, Dodgeball.WINDOW_HEIGHT -250f);
        }*/

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

        //fps = Gdx.graphics.getFramesPerSecond();

        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        camera.update();

        if (!freeze) {
            font.draw(batch, formatMin + ":" + formatSec, WIDTH - (layout.width + 50f),
                    HEIGHT - layout.height / 2);
        }
        /*fpsFont.draw(batch, (int)fps + " fps", Dodgeball.WINDOW_WIDTH / 400f,
                Dodgeball.WINDOW_HEIGHT - 100f);

        // Pelaajan liikkeen keskipiste x ja y
        if (x != 0 && y != 0) {
            fpsFont.draw(batch, "y: " + y, Dodgeball.WINDOW_WIDTH / 400f, Dodgeball.WINDOW_HEIGHT -200f);
            fpsFont.draw(batch, "x: " + x, Dodgeball.WINDOW_WIDTH / 400f, Dodgeball.WINDOW_HEIGHT -250f);
        }*/

        batch.end();
    }

    public void dispose() {
        generator.dispose();
        font.dispose();
        stage.dispose();
    }
}
