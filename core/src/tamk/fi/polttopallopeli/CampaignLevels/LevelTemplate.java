package tamk.fi.polttopallopeli.CampaignLevels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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

import tamk.fi.polttopallopeli.Balls;
import tamk.fi.polttopallopeli.ContactDetection;
import tamk.fi.polttopallopeli.Dodgeball;
import tamk.fi.polttopallopeli.GameTimer;
import tamk.fi.polttopallopeli.HeatMap;
import tamk.fi.polttopallopeli.Player;
import tamk.fi.polttopallopeli.Screens.Menu;

public class LevelTemplate implements Screen {
    private SpriteBatch batch;
    private Dodgeball host;
    private World world;
    private Body walls;
    private Player player;
    private Texture backgroundTexture;
    private OrthographicCamera camera;
    private Balls[] ball;
    private GameTimer timer;
    private Texture gameOver;
    private int[] ballLocator;
    private HeatMap heatMap;

    private final int MAX_BALL_AMOUNT = 10; // Maksimi määrä palloja kentällä yhtäaikaa.
    private final float BALL_SPAWN_TIMER = 3; // Kauanko odotetaan pallon tuloa alussa (ja jos useampi alussa niin kauanko niiden välillä). SEKUNTTI.
    private final int BALL_SPAWN_COUNT = 3; // Montako palloa lisätään alussa.
    private final int ADD_NEW_BALL_TIME = 60; // Koska lisätään uusi pallo alun jälkeen. SEKUNTTI.
    private boolean victory;
    private boolean defeat;

    private Box2DDebugRenderer debugRenderer;
    private float TIME_STEP = 1/60f;
    private int VELOCITY_ITERATIONS = 6;
    private int POSITION_ITERATIONS = 2;
    private float accumulator = 0;

    private long timeLimit;

    public LevelTemplate(Dodgeball host) {
        this.host = host;
        batch = host.getBatch();
        backgroundTexture = new Texture("background1.png");
        gameOver = new Texture("gameover.png");
        ballLocator = new int[32];
        heatMap = new HeatMap();
        ball = new Balls[MAX_BALL_AMOUNT];

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Dodgeball.WORLD_WIDTH, Dodgeball.WORLD_HEIGHT);

        debugRenderer = new Box2DDebugRenderer();
        world = new World(new Vector2(0, 0), true);
        player = new Player(world, batch);

        timer = new GameTimer(batch);

        world.setContactListener(new ContactDetection());

        //Tätä vaihtamalla vaihtuu kentän ajallinen pituus. Yksikkö on sekuntti.
        timeLimit = 60;

        victory = false;
        defeat = false;

        worldWalls();
    }

    @Override
    public void show() {

    }

    private boolean calculated = false;
    private float divideAmount;
    private float lastDelta;
    private float xCenter = 0;
    private float yCenter = 0;

    private void heatMapDataHandler(float delta) {
        lastDelta += delta;
        //Gdx.app.log(getClass().getSimpleName(), ""+ lastDelta);

        //HEATMAP DATA COLLECTION
        if (player.getHealth() > 0 && lastDelta > 0.25f) {
            divideAmount += 1;
            heatMap.modify(player.getPlayerBodyX(), player.getPlayerBodyY());

            if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
                xCenter += player.getAccelY();
                yCenter += player.getAccelZ();
            }

            lastDelta = 0;
        }

        //CENTERPOINT OF MOVEMENT CALCULATION
        if (!calculated && player.getHealth() == 0) {
            calculated = true;
            if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
                xCenter = xCenter / divideAmount;
                yCenter = yCenter / divideAmount;
                timer.setY(yCenter);
                timer.setX(xCenter);
                Gdx.app.log(getClass().getSimpleName(), "xCenter: " + xCenter);
                Gdx.app.log(getClass().getSimpleName(), "yCenter: " + yCenter);
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
                    ball[ballStartCounter] = new Balls(world, batch, getPlayerX(), getPlayerY(), ballLocator);
                    ballLocator[ball[ballStartCounter].getLocationToUpdateBallLocator()] = 1;
                    ballSpawnTimer = 0;
                    ballStartCounter++;
                }
                // Adds balls as game advances
            } else if (ballSpawnTimer > ADD_NEW_BALL_TIME && ballStartCounter < MAX_BALL_AMOUNT) {
                ball[ballStartCounter] = new Balls(world, batch, getPlayerX(), getPlayerY(), ballLocator);
                ballLocator[ball[ballStartCounter].getLocationToUpdateBallLocator()] = 1;
                ballSpawnTimer = 0;
                ballStartCounter++;
            }

            // Draws balls and checks if out of bounds, deletes and respawns a new one.
            int i = 0;
            for (Balls eachBall : ball) {
                if (eachBall != null) {
                    eachBall.draw(delta);
                    if (eachBall.getX() > Dodgeball.WORLD_WIDTH + 2 || eachBall.getY() > Dodgeball.WORLD_HEIGHT + 2 ||
                            eachBall.getX() < -2 || eachBall.getY() < -2) {
                        ballLocator[eachBall.getLocationToUpdateBallLocator()] = 0;
                        eachBall.dispose();
                        ball[i] = new Balls(world, batch, getPlayerX(), getPlayerY(), ballLocator);
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

        ballHandler(delta);

        player.drawHealth(delta);

        batch.begin();

        if (player.getHealth() == 0) {

            // Leveli screenin testailua
            /*LevelPreferences.prefs.putInteger("level2", 1);
            LevelPreferences.prefs.flush();

            System.out.println(LevelPreferences.prefs.getInteger("level2"));
            /*
            batch.draw(gameOver,Dodgeball.WORLD_WIDTH / 2 - gameOver.getWidth() / 100 / 2,
                    Dodgeball.WORLD_HEIGHT / 2 - gameOver.getHeight() / 100 / 2,
                    gameOver.getWidth() / 100, gameOver.getHeight() / 100);
                    */
            timer.setFreeze();
            heatMap.draw(batch);
            if (!victory) {
                defeat = true;
            }
        }

        batch.end();

        timer.levelModeTimer(timeLimit);
        if (timer.getElapsedTime() > timeLimit) {
            timer.setFreeze();
            if (!defeat) {
                victory = true;
                player.victory = true;
            }

            LevelPreferences.prefs.putInteger("level2", 1);
            LevelPreferences.prefs.flush();
        }

        //debugRenderer.render(world, camera.combined);
        doPhysicsStep(delta);

        // For testing purposes
        if (Gdx.input.isTouched() && player.getHealth() == 0) {
            host.setScreen(new Menu(host));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            host.setScreen(new Menu(host));
        }
    }

    private void doPhysicsStep(float deltaTime) {

        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            accumulator -= TIME_STEP;
        }
    }

    public float getPlayerX() {
        return player.getPlayerBodyX();
    }

    public float getPlayerY() {
        return player.getPlayerBodyY();
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
        backgroundTexture.dispose();
        gameOver.dispose();
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
        Gdx.app.log(getClass().getSimpleName(), "disposing");
    }
}