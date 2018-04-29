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
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import tamk.fi.polttopallopeli.Screens.HighScore;
import tamk.fi.polttopallopeli.Screens.Menu;

public class SurvivalMode implements Screen {
    private SpriteBatch batch;
    private Dodgeball host;
    private World world;
    private Body walls;
    private Player player;
    private Texture backgroundTexture;
    private OrthographicCamera camera;
    private Balls[] ball;
    private GameTimer timer;
    private int[] ballLocator;
    private HeatMap heatMap;
    private CenterOfPlayer center;
    private Vector2 centerPoint;

    private final int MAX_BALL_AMOUNT = 10;
    private final float BALL_SPAWN_TIMER = 3;
    private final int BALL_SPAWN_COUNT = 3;
    private final boolean ACCELERATING_BALL = true;
    private final boolean TARGETING_BALL = true;
    private final boolean FASTBALL = true;
    private final boolean HEALINGBALL = true;
    private final int ADD_NEW_BALL_TIME = 30; // Koska lisätään uusi pallo alun jälkeen. SEKUNTTI. esim: 60

    private Box2DDebugRenderer debugRenderer;
    private float TIME_STEP = 1/60f;
    private int VELOCITY_ITERATIONS = 6;
    private int POSITION_ITERATIONS = 2;
    private float accumulator = 0;

    final int colWidth = Gdx.graphics.getWidth() / 12;
    final int rowHeight = Gdx.graphics.getHeight() / 12;
    final float WIDTH = Gdx.graphics.getWidth();

    private Music music;
    private Stage stage;
    private Skin skin;
    private Label gameOverText;
    private TextButton playAgain;
    private TextButton menu;
    private TextButton heat;
    private TextButton playerMovement;

    public SurvivalMode(Dodgeball host) {
        this.host = host;
        batch = host.getBatch();
        stage = new Stage(new ScreenViewport(), batch);

        music = Dodgeball.manager.get("survival.ogg", Music.class);

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

        debugRenderer = new Box2DDebugRenderer();
        world = new World(new Vector2(0, 0), true);
        player = new Player(world, batch);

        timer = new GameTimer(batch, false, stage);

        world.setContactListener(new ContactDetection());

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

    private void isGameOver() {
        if (player.getHealth() == 0) {
            /*
            batch.draw(gameOver,Dodgeball.WORLD_WIDTH / 2 - gameOver.getWidth() / 100 / 2,
                    Dodgeball.WORLD_HEIGHT / 2 - gameOver.getHeight() / 100 / 2,
                    gameOver.getWidth() / 100, gameOver.getHeight() / 100);
                    */
            timer.setFreeze();
            //heatMap.draw(batch);
            center.draw(batch, camera);

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

        if (timer.getElapsedTime() >= 4) {
            ballHandler(delta);
        }

        player.drawHealth(delta);
        isGameOver();

        timer.survivalModeTimer();

        //debugRenderer.render(world, camera.combined);
        doPhysicsStep(delta);

        if (player.getHealth() == 0) {
            stage.addActor(gameOverText);
            stage.addActor(playAgain);
            stage.addActor(menu);
            stage.addActor(heat);
            stage.addActor(playerMovement);
        }

        timer.countDownTimer();
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

    public void setHighScore() {
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
            Dodgeball.manager.get("highscore.ogg", Sound.class).play(Dodgeball.VOLUME);
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

    public void gameOverScreen() {
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
        music.stop();
        backgroundTexture.dispose();
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