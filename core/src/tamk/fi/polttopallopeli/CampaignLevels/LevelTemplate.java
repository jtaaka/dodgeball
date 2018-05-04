package tamk.fi.polttopallopeli.CampaignLevels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
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

import tamk.fi.polttopallopeli.Balls;
import tamk.fi.polttopallopeli.CenterOfPlayer;
import tamk.fi.polttopallopeli.ContactDetection;
import tamk.fi.polttopallopeli.Dodgeball;
import tamk.fi.polttopallopeli.GameTimer;
import tamk.fi.polttopallopeli.HeatMap;
import tamk.fi.polttopallopeli.LevelPreferences;
import tamk.fi.polttopallopeli.Player;
import tamk.fi.polttopallopeli.Screens.Menu;

/**
 * Template class for all levels.
 *
 * @author  Juho Taakala <juho.taakala@cs.tamk.fi>
 *          Joni Alanko <joni.alanko@cs.tamk.fi>
 * @since   2018.0222
 * @version 1.0
 */
public class LevelTemplate implements Screen {

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
     * Defines secret level's texture.
     */
    private Texture secretTexture;

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
    int MAX_BALL_AMOUNT; // Maksimi määrä palloja kentällä yhtäaikaa. esim: 10

    /**
     * Defines ball spawn timer.
     */
    float BALL_SPAWN_TIMER;  // Kauanko odotetaan pallon tuloa alussa (ja jos useampi alussa niin kauanko niiden välillä). SEKUNTTI. esim: 4

    /**
     * Defines how many balls are added at the start.
     */
    int BALL_SPAWN_COUNT; // Montako palloa lisätään alussa. esim: 3

    /**
     * Defines the time to add new ball to the world.
     */
    int ADD_NEW_BALL_TIME; // Koska lisätään uusi pallo alun jälkeen. SEKUNTTI. esim: 60

    /**
     * Defines if there is an accelerating ball or not.
     */
    boolean ACCELERATING_BALL; // onko levelissä kiihtyvää palloa. true / false

    /**
     * Defines if there is a targeting ball or not.
     */
    boolean TARGETING_BALL; // onko levelissä ennakoivaa palloa. true / false

    /**
     * Defines if there is a fast ball or not.
     */
    boolean FASTBALL; // onko levelissä nopeampaa palloa. true / false

    /**
     * Defines if there is a healing ball or not.
     */
    boolean HEALINGBALL; // onko levelissä parantavaa palloa. true / false

    /**
     * Defines the timelimit for the level.
     */
    long timeLimit; //Tätä vaihtamalla vaihtuu kentän ajallinen pituus. Yksikkö on sekuntti. esim: 60

    /**
     * Defines the next level.
     */
    public String nextLevel; // Seuraava avautuva kenttä. Esimerkiksi: "level2"

    /**
     * Defines a music for the level.
     */
    private Music music;

    /**
     * Defines a music for the level.
     */
    private Music music2;

    /**
     * Defines a music for the level.
     */
    private Music music3;

    /**
     * Tells if a player won or not.
     */
    private boolean victory;

    /**
     * Tells if a player was defeated.
     */
    private boolean defeat;

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
     * Defines stage for the level.
     */
    private Stage stage;

    /**
     * Defines a skin for button styles.
     */
    private Skin skin;

    /**
     * Defines game over text label.
     */
    private Label gameOverText;

    /**
     * Defines victory text label.
     */
    private Label victoryText;

    /**
     * Defines play again button.
     */
    private TextButton playAgain;

    /**
     * Defines next level button.
     */
    private TextButton nextLevelButton;

    /**
     * Defines menu button.
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
     * Level template constructor.
     *
     * @param host "main class" host.
     * @param MAX_BALL_AMOUNT maximum number of balls on the screen.
     * @param background background texture.
     * @param whiteTimer white timer or not.
     */
    public LevelTemplate(Dodgeball host, int MAX_BALL_AMOUNT, Texture background, boolean whiteTimer) {
        this.host = host;
        this.MAX_BALL_AMOUNT = MAX_BALL_AMOUNT;
        batch = host.getBatch();
        stage = new Stage(new ScreenViewport(), batch);

        backgroundTexture = background;

        ballLocator = new int[32];
        heatMap = new HeatMap();
        center = new CenterOfPlayer();
        centerPoint = new Vector2();
        ball = new Balls[MAX_BALL_AMOUNT];
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Dodgeball.WORLD_WIDTH, Dodgeball.WORLD_HEIGHT);
        //debugRenderer = new Box2DDebugRenderer();
        world = new World(new Vector2(0, 0), true);
        player = new Player(world, batch);
        timer = new GameTimer(batch, whiteTimer, stage);
        world.setContactListener(new ContactDetection(host));
        victory = false;
        defeat = false;

        Gdx.input.setCatchBackKey(true);

        playMusic();
        gameOverScreen();
        worldWalls();
    }

    @Override
    public void show() {
    }

    /**
     *
     */
    private boolean calculated = false;

    /**
     *
     */
    private float divideAmount;

    /**
     *
     */
    private float lastDelta;

    private void playMusic() {
        if (Dodgeball.MUSIC_VOLUME > 0) {
            if (music != null) {
                music.dispose();
            }
            switch (MathUtils.random(1,2)) {
                case 1:
                    music = host.getManager("India.ogg", Music.class);
                    music.setLooping(true);
                    music.play();
                    break;
                case 2:
                    music = host.getManager("survival.ogg", Music.class);
                    music.setLooping(true);
                    music.play();
                    break;
            }
        }
    }

    /**
     * Draws heatmap.
     *
     * @param delta is deltatime.
     */
    private void heatMapDataHandler(float delta) {
        lastDelta += delta;
        //Gdx.app.log(getClass().getSimpleName(), ""+ lastDelta);
        //HEATMAP DATA COLLECTION
        if (player.getHealth() > 0 && lastDelta > 0.25f && !victory) {
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
        //CENTERPOINT OF MOVEMENT CALCULATION
        if (!calculated && player.getHealth() == 0 || !calculated && victory) {
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

    /**
     *
     */
    private float ballSpawnTimer = 0;

    /**
     *
     */
    private int ballStartCounter = 0;

    /**
     * Handles ball spawning and disposing.
     *
     * @param delta is deltatime.
     */
    private void ballHandler(float delta) {
        if (!victory) {
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
            // Draws balls and checks if out of bounds, deletes and respawns a new one.
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
        } else {
            for (Balls eachBall : ball) {
                if (eachBall != null) {
                    eachBall.outOfSightOutOfMind();
                }
            }
        }
    }

    /**
     * Adds button actors.
     */
    private void addActors() {

        if (player.getHealth() == 0) {
            stage.addActor(gameOverText);
            stage.addActor(playAgain);
            stage.addActor(menu);
            stage.addActor(heat);
            stage.addActor(playerMovement);
        }

        if (victory && !nextLevel.equals("level11") && !nextLevel.equals("secret")) {
            stage.addActor(victoryText);
            stage.addActor(nextLevelButton);
            stage.addActor(menu);
            stage.addActor(heat);
            stage.addActor(playerMovement);
        }

        if (victory && nextLevel.equals("level11") || victory && nextLevel.equals("secret")) {
            stage.addActor(victoryText);
            stage.addActor(menu);
            stage.addActor(heat);
            stage.addActor(playerMovement);
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
        System.out.println(timer.getElapsedTime());


        if (timer.getElapsedTime() - (TimeUtils.nanosToMillis(pauseDelay) / 1000)  >= 4) {
            ballHandler(delta);
        }
        
        if (nextLevel.equals("secret")) {
            secretLevel(delta);
        }

        player.playerMove(delta);
        player.drawHealth(delta);

        isGameOver();
        doPhysicsStep(delta);
        addActors();

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

    private Batch secretBatch;
    float alpha = 0;
    boolean rising = true;
    boolean blackening = false;
    float blackeningTimer;

    private void secretLevel(float delta) {
        if (secretTexture == null) {
            secretTexture = new Texture("black.png");
            secretBatch = new SpriteBatch();
            secretBatch.setColor(1,1,1,0);
            if (Dodgeball.MUSIC_VOLUME > 0) {
                if (music != null) {
                    music.dispose();
                }

                music = host.getManager("Clucth.ogg", Music.class);
                music.setLooping(true);
                music.play();
            }
        }

        blackeningTimer += delta;
        if (blackeningTimer > 5) {
            blackening = true;
        }

        if (blackening) {
            if (alpha < 1f && rising) {
                alpha += 0.5f * delta;
                if (alpha > 1) {
                    alpha = 1f;
                }
            } else if (alpha > 0f) {
                alpha -= 0.5f * delta;
                if (alpha < 0) {
                    alpha = 0f;
                }
                rising = false;
            } else {
                rising = true;
                blackening = false;
                blackeningTimer = 0;
            }
        }

        secretBatch.setColor(1,1,1,alpha);

        secretBatch.setProjectionMatrix(camera.combined);

        secretBatch.begin();
        if (!victory && !defeat) {
            secretBatch.draw(secretTexture, 0, 0, Dodgeball.WORLD_WIDTH, Dodgeball.WORLD_HEIGHT);
        }
        secretBatch.end();
    }

    /**
     * Checks if the game is over.
     */
    private void isGameOver() {

        if (player.getHealth() == 0 && !victory) {
            timer.setFreeze();
            defeat = true;
        }

        if (victory || defeat) {
            if (showHeat) {
                heatMap.draw(batch);
            }

            if (showMovement) {
                center.draw(batch, camera);
            }
        }

        if (timer.getElapsedTime() - (TimeUtils.nanosToMillis(pauseDelay) / 1000) <= timeLimit) {
            timer.levelModeTimer(timeLimit, pauseDelay);
        }

        if (timer.getElapsedTime() - (TimeUtils.nanosToMillis(pauseDelay) / 1000) > timeLimit && !defeat) {
            timer.setFreeze();
            victory = true;
            player.victory = true;
            LevelPreferences.prefs.putInteger(nextLevel, 1); // minkä mapin läpipeluu avaa esim. "level2"
            LevelPreferences.prefs.flush();
        }
    }

    /**
     * Adds game over screen buttons when the game is over.
     */
    private void gameOverScreen() {
        skin = new Skin(Gdx.files.internal("menu.json"));

        gameOverText = new Label(host.getLang().get("gameover"), skin, "default");
        gameOverText.setFontScale(2f, 2f);
        gameOverText.setPosition(WIDTH / 2 - gameOverText.getWidth(), rowHeight * 8f);

        victoryText = new Label(host.getLang().get("victory"), skin, "default");
        victoryText.setFontScale(2f, 2f);
        victoryText.setPosition(WIDTH / 2 - victoryText.getWidth(), rowHeight * 8f);

        playAgain = new TextButton(host.getLang().get("playagain"), skin,"default");
        playAgain.setSize(colWidth * 3f, rowHeight);
        playAgain.setPosition(WIDTH / 2 - (playAgain.getWidth() / 2f), rowHeight * 6f);

        nextLevelButton = new TextButton(host.getLang().get("nextlevel"), skin,"default");
        nextLevelButton.setSize(colWidth * 3f, rowHeight);
        nextLevelButton.setPosition(WIDTH / 2 - (playAgain.getWidth() / 2f), rowHeight * 6f);

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

                if (nextLevel.equals("level2")) {
                    host.setScreen(new Level1(host));
                }

                if (nextLevel.equals("level3")) {
                    host.setScreen(new Level2(host));
                }

                if (nextLevel.equals("level4")) {
                    host.setScreen(new Level3(host));
                }

                if (nextLevel.equals("level5")) {
                    host.setScreen(new Level4(host));
                }

                if (nextLevel.equals("level6")) {
                    host.setScreen(new Level5(host));
                }

                if (nextLevel.equals("level7")) {
                    host.setScreen(new Level6(host));
                }

                if (nextLevel.equals("level8")) {
                    host.setScreen(new Level7(host));
                }

                if (nextLevel.equals("level9")) {
                    host.setScreen(new Level8(host));
                }

                if (nextLevel.equals("level10")) {
                    host.setScreen(new Level9(host));
                }

                if (nextLevel.equals("level11")) {
                    host.setScreen(new Level10(host));
                }

                if (nextLevel.equals("secret")) {
                    host.setScreen(new Level11(host));
                }

                playMusic();
            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        nextLevelButton.addListener(new InputListener() {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

                if (nextLevel.equals("level2")) {
                    host.setScreen(new Level2(host));
                }

                if (nextLevel.equals("level3")) {
                    host.setScreen(new Level3(host));
                }

                if (nextLevel.equals("level4")) {
                    host.setScreen(new Level4(host));
                }

                if (nextLevel.equals("level5")) {
                    host.setScreen(new Level5(host));
                }

                if (nextLevel.equals("level6")) {
                    host.setScreen(new Level6(host));
                }

                if (nextLevel.equals("level7")) {
                    host.setScreen(new Level7(host));
                }

                if (nextLevel.equals("level8")) {
                    host.setScreen(new Level8(host));
                }

                if (nextLevel.equals("level9")) {
                    host.setScreen(new Level9(host));
                }

                if (nextLevel.equals("level10")) {
                    host.setScreen(new Level10(host));
                }
            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        menu.addListener(new InputListener() {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
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
        backgroundTexture.dispose();
        if (secretTexture != null) {
            secretTexture.dispose();
        }

        if (music != null) {
            music.stop();
        }

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
        if (secretBatch != null) {
            secretBatch.dispose();
        }
        //Gdx.app.log(getClass().getSimpleName(), "disposing");
    }
}
