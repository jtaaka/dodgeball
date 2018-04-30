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

public class LevelTemplate implements Screen {
    private SpriteBatch batch;
    private Dodgeball host;
    private World world;
    private Body walls;
    private Player player;
    private Texture backgroundTexture;
    private Texture secretTexture;
    private OrthographicCamera camera;
    private Balls[] ball;
    private GameTimer timer;
    private int[] ballLocator;
    private HeatMap heatMap;
    private CenterOfPlayer center;
    private Vector2 centerPoint;

    int MAX_BALL_AMOUNT; // Maksimi määrä palloja kentällä yhtäaikaa. esim: 10
    float BALL_SPAWN_TIMER;  // Kauanko odotetaan pallon tuloa alussa (ja jos useampi alussa niin kauanko niiden välillä). SEKUNTTI. esim: 4
    int BALL_SPAWN_COUNT; // Montako palloa lisätään alussa. esim: 3
    int ADD_NEW_BALL_TIME; // Koska lisätään uusi pallo alun jälkeen. SEKUNTTI. esim: 60
    boolean ACCELERATING_BALL; // onko levelissä kiihtyvää palloa. true / false
    boolean TARGETING_BALL; // onko levelissä ennakoivaa palloa. true / false
    boolean FASTBALL; // onko levelissä nopeampaa palloa. true / false
    boolean HEALINGBALL; // onko levelissä parantavaa palloa. true / false
    long timeLimit; //Tätä vaihtamalla vaihtuu kentän ajallinen pituus. Yksikkö on sekuntti. esim: 60
    public String nextLevel; // Seuraava avautuva kenttä. Esimerkiksi: "level2"

    private Music music;

    private boolean victory;
    private boolean defeat;
    //private Box2DDebugRenderer debugRenderer;
    private float TIME_STEP = 1/60f;
    private int VELOCITY_ITERATIONS = 6;
    private int POSITION_ITERATIONS = 2;
    private float accumulator = 0;

    final int colWidth = Gdx.graphics.getWidth() / 12;
    final int rowHeight = Gdx.graphics.getHeight() / 12;
    final float WIDTH = Gdx.graphics.getWidth();

    private Stage stage;
    private Skin skin;
    private Label gameOverText;
    private Label victoryText;
    private TextButton playAgain;
    private TextButton nextLevelButton;
    private TextButton menu;
    private TextButton heat;
    private TextButton playerMovement;

    private long pauseTime = 0;
    private long pauseDelay = 0;

    public LevelTemplate(Dodgeball host, int MAX_BALL_AMOUNT, Texture background, boolean whiteTimer) {
        this.host = host;
        this.MAX_BALL_AMOUNT = MAX_BALL_AMOUNT;
        batch = host.getBatch();
        stage = new Stage(new ScreenViewport(), batch);

        music = Dodgeball.manager.get("Clucth.ogg", Music.class);

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
        world.setContactListener(new ContactDetection());
        victory = false;
        defeat = false;

        gameOverScreen();
        worldWalls();
    }

    @Override
    public void show() {
    }

    private boolean calculated = false;
    private float divideAmount;
    private float lastDelta;

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

    private float ballSpawnTimer = 0;
    private int ballStartCounter = 0;

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

        //debugRenderer.render(world, camera.combined);
        doPhysicsStep(delta);

        /*// For testing purposes
        if (Gdx.input.isTouched() && player.getHealth() == 0 || victory && Gdx.input.isTouched()) {
            host.setScreen(new Menu(host));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            host.setScreen(new Menu(host));
        }*/

        addActors();

        timer.countDownTimer((TimeUtils.nanosToMillis(pauseDelay) / 1000));
        stage.act(delta);
        stage.draw();
    }

    private void doPhysicsStep(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            accumulator -= TIME_STEP;
        }
    }

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

    Batch secretBatch;
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

    private void isGameOver() {
        //batch.begin();

        if (player.getHealth() == 0 && !victory) {
            timer.setFreeze();
            defeat = true;
        }

        if (victory || defeat) {
            //heatMap.draw(batch);
            center.draw(batch, camera);
        }

        //batch.end();

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

    public void gameOverScreen() {
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

            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        playerMovement.addListener(new InputListener() {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

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
        music.stop();
        //gameOver.dispose();
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
