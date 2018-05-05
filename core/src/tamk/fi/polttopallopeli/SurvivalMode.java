package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import tamk.fi.polttopallopeli.Screens.HighScore;
import tamk.fi.polttopallopeli.Screens.Menu;

/**
 * Class for the survival mode.
 *
 * @author  Juho Taakala <juho.taakala@cs.tamk.fi>
 *          Joni Alanko <joni.alanko@cs.tamk.fi>
 * @since   2018.0222
 * @version 1.0
 */
public class SurvivalMode implements Screen {

    /**
     * Defines SpriteBatch for level template.
     */
    private SpriteBatch batch;

    /**
     * Defines "main class" as a host.
     */
    private Dodgeball host;

    /**
     * Defines world.
     */
    private World world;

    /**
     * Defines world walls.
     */
    private Body walls;

    /**
     * Defines player.
     */
    private Player player;

    /**
     * Defines background texture for world.
     */
    private Texture backgroundTexture;

    /**
     * Defines camera for the world.
     */
    private OrthographicCamera camera;

    /**
     * Defines balls for the world.
     */
    private Balls[] ball;

    /**
     * Defines game timer.
     */
    private GameTimer timer;

    /**
     * Defines ball locator.
     */
    private int[] ballLocator;

    /**
     * Defines heatmap for player movement.
     */
    private HeatMap heatMap;

    /**
     * Defines center of the player.
     */
    private CenterOfPlayer center;

    /**
     * Defines center point.
     */
    private Vector2 centerPoint;

    /**
     * Defines max ball amount in world.
     */
    private final int MAX_BALL_AMOUNT = 10;

    /**
     * Defines ball spawn timer.
     */
    private final float BALL_SPAWN_TIMER = 3;

    /**
     * Defines how many balls are added at the start.
     */
    private final int BALL_SPAWN_COUNT = 3;

    /**
     * Defines if there is an accelerating ball or not.
     */
    private final boolean ACCELERATING_BALL = true;

    /**
     * Defines if there is a targeting ball or not.
     */
    private final boolean TARGETING_BALL = true;

    /**
     * Defines if there is a fast ball or not.
     */
    private final boolean FASTBALL = true;

    /**
     * Defines if there is a healing ball or not.
     */
    private final boolean HEALINGBALL = true;

    /**
     * Defines the time to add new ball to the world.
     */
    private final int ADD_NEW_BALL_TIME = 30; // Koska lisätään uusi pallo alun jälkeen. SEKUNTTI. esim: 60

    //private Box2DDebugRenderer debugRenderer;

    /**
     * Defines time step.
     */
    private float TIME_STEP = 1/60f;

    /**
     * Defines velocity iterations.
     */
    private int VELOCITY_ITERATIONS = 6;

    /**
     * Defines position iterations.
     */
    private int POSITION_ITERATIONS = 2;

    /**
     * Defines the accumulator for physics steps.
     */
    private float accumulator = 0;

    /**
     * Defines device's screen column width to help position buttons.
     */
    final int colWidth = Gdx.graphics.getWidth() / 12;

    /**
     * Defines device's screen row height to help position buttons.
     */
    final int rowHeight = Gdx.graphics.getHeight() / 12;

    /**
     * Defines device's screen width.
     */
    final float WIDTH = Gdx.graphics.getWidth();

    /**
     * Defines music for the game.
     */
    private Music music;

    /**
     * Defines stage for the level.
     */
    private Stage stage;

    /**
     * Defines skin for texts and buttons.
     */
    private Skin skin;

    /**
     * Defines game over text label.
     */
    private Label gameOverText;

    /**
     * Defines play again button.
     */
    private TextButton playAgain;

    /**
     * defines menu button.
     */
    private TextButton menu;

    /**
     * Defines heatmap drawing button.
     */
    private TextButton heat;

    /**
     * Defines player movement drawing button.
     */
    private TextButton playerMovement;

    /**
     * Defines if movement drawing is shown or not.
     */
    private boolean showMovement = false;

    /**
     * Defines if heatmap drawing is shown or not.
     */
    private boolean showHeat = false;

    /**
     * Defines the time when game is paused.
     */
    private long pauseTime = 0;

    /**
     * Defines the time that was spent in pause mode.
     */
    private long pauseDelay = 0;

    /**
     * Constructor for survival mode.
     *
     * @param host "main class" host.
     */
    public SurvivalMode(Dodgeball host) {
        this.host = host;
        batch = host.getBatch();
        stage = new Stage(new ScreenViewport(), batch);

        if (music != null) {
            music.dispose();
        }

        music = host.getManager("survival.ogg", Music.class);

        if (Dodgeball.MUSIC_VOLUME > 0) {
            music.setLooping(true);
            music.play();
        }

        backgroundTexture = new Texture("background1.png");
        ballLocator = new int[32];
        heatMap = new HeatMap();
        center = new CenterOfPlayer();
        centerPoint = new Vector2();
        ball = new Balls[MAX_BALL_AMOUNT];

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Dodgeball.WORLD_WIDTH, Dodgeball.WORLD_HEIGHT);

        world = new World(new Vector2(0, 0), true);
        player = new Player(world, batch);

        timer = new GameTimer(batch, false, stage);

        world.setContactListener(new ContactDetection(host));
        Gdx.input.setCatchBackKey(true);

        gameOverScreen();
        worldWalls();
    }

    @Override
    public void show() {

    }

    private boolean calculated = false;
    private float divideAmount;
    private float lastDelta;

    /**
     * Handles heatmap and center point data logging.
     *
     * @param delta is deltatime.
     */
    private void heatMapDataHandler(float delta) {
        //HEATMAP DATA COLLECTION
        //Gdx.app.log(getClass().getSimpleName(), ""+ lastDelta);
        lastDelta += delta;

        if (player.getHealth() > 0 && lastDelta > 0.25f) {
            divideAmount += 1;
            heatMap.modify(player.getPlayerBodyX(), player.getPlayerBodyY());

            if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
                centerPoint.x += player.getAccelY();
                centerPoint.y += player.getAccelZ();
                if (!player.hit) {
                    center.modify(player.getAccelY(), player.getAccelZ());
                }
            }

            lastDelta = 0;
        }

        if (!calculated && player.getHealth() == 0) {
            calculated = true;
            if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
                centerPoint.x = centerPoint.x / divideAmount;
                centerPoint.y = centerPoint.y / divideAmount;
                timer.setX(centerPoint.x);
                timer.setY(centerPoint.y);
                Gdx.app.log(getClass().getSimpleName(), "xCenter: " + centerPoint.x);
                Gdx.app.log(getClass().getSimpleName(), "yCenter: " + centerPoint.y);
                center.calculatedCenter(centerPoint);
            }
        }
    }

    private float ballSpawnTimer = 0;
    private int ballStartCounter = 0;

    /**
     * Handles ball spawning and disposing.
     *
     * @param delta is deltatime.
     */
    private void ballHandler(float delta) {
        // Determines ball spawning at the start
        ballSpawnTimer += delta;
        if (ballStartCounter < BALL_SPAWN_COUNT) {
            if (ballSpawnTimer > BALL_SPAWN_TIMER) {
                ball[ballStartCounter] = new Balls(world, batch, player.getBody(), ballLocator, ACCELERATING_BALL, TARGETING_BALL, FASTBALL, HEALINGBALL);
                ballLocator[ball[ballStartCounter].getLocationToUpdateBallLocator()] = 1;
                ballSpawnTimer = 0;
                ballStartCounter++;
            }
            // Adds balls as game advances
        } else if (ballSpawnTimer > ADD_NEW_BALL_TIME && ballStartCounter < MAX_BALL_AMOUNT) {
            ball[ballStartCounter] = new Balls(world, batch, player.getBody(), ballLocator, ACCELERATING_BALL, TARGETING_BALL, FASTBALL, HEALINGBALL);
            ballLocator[ball[ballStartCounter].getLocationToUpdateBallLocator()] = 1;
            ballSpawnTimer = 0;
            ballStartCounter++;
        }

        int i = 0;
        for (Balls eachBall : ball) {
            if (eachBall != null) {
                eachBall.draw(delta);
                if (eachBall.healingUsed) {
                    ballLocator[eachBall.getLocationToUpdateBallLocator()] = 0;
                    eachBall.dispose();
                    ball[i] = new Balls(world, batch, player.getBody(), ballLocator, ACCELERATING_BALL, TARGETING_BALL, FASTBALL, HEALINGBALL);
                    ballLocator[ball[i].getLocationToUpdateBallLocator()] = 1;
                }
                if (eachBall.getX() > Dodgeball.WORLD_WIDTH + 2 || eachBall.getY() > Dodgeball.WORLD_HEIGHT + 2 ||
                        eachBall.getX() < -2 || eachBall.getY() < -2) {
                    ballLocator[eachBall.getLocationToUpdateBallLocator()] = 0;
                    eachBall.dispose();
                    ball[i] = new Balls(world, batch, player.getBody(), ballLocator, ACCELERATING_BALL, TARGETING_BALL, FASTBALL, HEALINGBALL);
                    ballLocator[ball[i].getLocationToUpdateBallLocator()] = 1;
                    //Gdx.app.log(getClass().getSimpleName(), "respawning");
                }
            }
            i++;
        }
    }

    boolean setScore = true;

    /**
     * Checks if the game is over.
     */
    private void isGameOver() {
        if (player.getHealth() == 0) {
            /*
            batch.draw(gameOver,Dodgeball.WORLD_WIDTH / 2 - gameOver.getWidth() / 100 / 2,
                    Dodgeball.WORLD_HEIGHT / 2 - gameOver.getHeight() / 100 / 2,
                    gameOver.getWidth() / 100, gameOver.getHeight() / 100);
                    */
            timer.setFreeze();
            if (showHeat) {
                heatMap.draw(batch);
            }

            if (showMovement) {
                center.draw(batch, camera);
            }

            if (setScore) {
                setHighScore();
                setScore = false;
            }

        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        heatMapDataHandler(delta);

        camera.update();

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Dodgeball.WORLD_WIDTH, Dodgeball.WORLD_HEIGHT);
        batch.end();

        player.playerMove(delta);

        if (timer.getElapsedTime() - (TimeUtils.nanosToMillis(pauseDelay) / 1000) >= 4) {
            ballHandler(delta);
        }

        player.drawHealth(delta);
        isGameOver();

        timer.survivalModeTimer(pauseDelay);

        //debugRenderer.render(world, camera.combined);
        doPhysicsStep(delta);

        if (player.getHealth() == 0) {
            stage.addActor(gameOverText);
            stage.addActor(playAgain);
            stage.addActor(menu);
            stage.addActor(heat);
            stage.addActor(playerMovement);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            host.setScreen(new Menu(host));
        }

        timer.countDownTimer((TimeUtils.nanosToMillis(pauseDelay) / 1000));
        stage.act(delta);
        stage.draw();
    }

    /**
     * Makes sure that the world step time is constant.
     *
     * @param deltaTime is deltatime.
     */
    private void doPhysicsStep(float deltaTime) {

        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            accumulator -= TIME_STEP;
        }
    }

    /**
     * Sets the high score times and names from profile preferences.
     */
    private void setHighScore() {
        long time = timer.getHighScoreTime();
        String score = "score";
        String profile = "profile";
        String name;

        HighScore.getScore();
        HighScore.setScore();
        ProfilePreferences.getprofile();
        ProfilePreferences.setProfile();

        if (Menu.profile.getText().equals("")) {
            name = "Anonymous";
        } else {
            name = Menu.profile.getText();
        }

        if (time > HighScore.scores[0]) {
            host.getManager("highscore.ogg", Sound.class).play(Dodgeball.VOLUME);
        }

        for (int i = 0; i < HighScore.scores.length; i++) {
            if (time >= HighScore.scores[i]) {

                for (int j = 9; j > i; j--) {
                    long putScore = HighScore.prefs.getLong(score + (j - 1));
                    HighScore.prefs.putLong(score + j, putScore);
                    HighScore.prefs.flush();

                    String putName = ProfilePreferences.prefs.getString(profile + (j - 1));
                    ProfilePreferences.prefs.putString(profile + j, putName);
                    ProfilePreferences.prefs.flush();
                }

                score = score + i;
                HighScore.prefs.putLong(score, time);
                HighScore.prefs.flush();

                profile = profile + i;
                ProfilePreferences.prefs.putString(profile, name);
                ProfilePreferences.prefs.flush();
                break;
            }
        }
    }

    /**
     * Defines world walls.
     */
    private void worldWalls() {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, 0);

        walls = world.createBody(bodyDef);

        ChainShape shape = new ChainShape();

        shape.createChain(new float[] {
                0, 0,
                Dodgeball.WORLD_WIDTH, 0,
                Dodgeball.WORLD_WIDTH, Dodgeball.WORLD_HEIGHT,
                0, Dodgeball.WORLD_HEIGHT,
                0, 0
        });

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = Dodgeball.OBJECT_WALL;
        fixtureDef.filter.maskBits = Dodgeball.OBJECT_PLAYER;
        fixtureDef.restitution = 0.6f;
        fixtureDef.shape = shape;

        walls.createFixture(fixtureDef);
        shape.dispose();
    }

    /**
     * Adds game over screen buttons when the game is over.
     */
    private void gameOverScreen() {
        skin = new Skin(Gdx.files.internal("menu.json"));

        gameOverText = new Label(host.getLang().get("gameover"), skin, "default");
        gameOverText.setFontScale(2f, 2f);
        gameOverText.setPosition(WIDTH / 2 - gameOverText.getWidth(), rowHeight * 8f);

        playAgain = new TextButton(host.getLang().get("playagain"), skin,"default");
        playAgain.setSize(colWidth * 3f, rowHeight);
        playAgain.setPosition(WIDTH / 2 - (playAgain.getWidth() / 2f), rowHeight * 6f);

        menu = new TextButton(host.getLang().get("menu"), skin, "default");
        menu.setSize(colWidth * 3f, rowHeight);
        menu.setPosition(WIDTH / 2 - (menu.getWidth() / 2f), rowHeight * 4.8f);

        heat = new TextButton(host.getLang().get("heatmap"), skin, "default");
        heat.setSize(colWidth * 3f, rowHeight);
        heat.setPosition(WIDTH / 2 - (heat.getWidth() / 2f), rowHeight * 3.6f);

        playerMovement = new TextButton(host.getLang().get("playermovement"), skin, "default");
        playerMovement.setSize(colWidth * 3f, rowHeight);
        playerMovement.setPosition(WIDTH / 2 - (playerMovement.getWidth() / 2f), rowHeight * 2.4f);

        playAgain.addListener(new InputListener() {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                host.setScreen(new SurvivalMode(host));
                music.play();
            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        menu.addListener(new InputListener() {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                music.dispose();
                host.setScreen(new Menu(host));
            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        heat.addListener(new InputListener() {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if (showHeat) {
                    showHeat = false;
                } else {
                    showHeat = true;
                }
            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        playerMovement.addListener(new InputListener() {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if (showMovement) {
                    showMovement = false;
                } else {
                    showMovement = true;
                }
            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        pauseTime = TimeUtils.nanoTime() - pauseDelay;
    }

    @Override
    public void resume() {
        pauseDelay = TimeUtils.timeSinceNanos(pauseTime);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        music.stop();
        backgroundTexture.dispose();
        for (Balls eachBall : ball) {
            if (eachBall != null) {
                eachBall.dispose();
            }
        }
        player.dispose();
        timer.dispose();
        heatMap.dispose();
        world.destroyBody(walls);
        world.dispose();
        stage.dispose();
        skin.dispose();
        Gdx.app.log(getClass().getSimpleName(), "disposing");
    }
}