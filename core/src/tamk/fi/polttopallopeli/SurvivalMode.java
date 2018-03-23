package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class SurvivalMode implements Screen {
    private SpriteBatch batch;
    private Dodgeball host;
    private World world;
    private Body walls;
    private Player player;
    private Texture backgroundTexture;
    private OrthographicCamera camera;
    //private Balls[] ball;
    private GameTimer timer;
    private Texture health;
    private Texture gameOver;
    private int[] ballLocator;
    Array<Vector2> heatMapData;
    private HeatMap heatMap;
    Array<Balls> ballsArray;

    private Box2DDebugRenderer debugRenderer;
    private float TIME_STEP = 1/60f;
    private int VELOCITY_ITERATIONS = 6;
    private int POSITION_ITERATIONS = 2;
    private float accumulator = 0;

    public SurvivalMode(Dodgeball host) {
        this.host = host;
        batch = host.getBatch();
        backgroundTexture = new Texture("background1.png");
        health = new Texture("life.png");
        gameOver = new Texture("gameover.png");
        ballLocator = new int[32];
        heatMapData = new Array<Vector2>();
        heatMap = new HeatMap();
        ballsArray = new Array<Balls>();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Dodgeball.WORLD_WIDTH, Dodgeball.WORLD_HEIGHT);

        debugRenderer = new Box2DDebugRenderer();
        world = new World(new Vector2(0, 0), true);
        player = new Player(world, batch);

        /*
        ball = new Balls[3];
        for (int i = 0; i < ball.length; i++) {
            ball[i] = new Balls(world, batch, getPlayerX(), getPlayerY(), ballLocator);
            ballLocator[ball[i].getLocationToUpdateBallLocator()] = 1;
        }
        */

        timer = new GameTimer(batch);

        world.setContactListener(new ContactDetection());

        worldWalls();
    }

    @Override
    public void show() {

    }

    private boolean calculated = false;
    private float divideAmount;
    private float lastDelta;
    private float ballSpawnTimer = 0;
    private int ballStartCounter = 0;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        /*
        lastDelta += delta;
        //Gdx.app.log(getClass().getSimpleName(), ""+ lastDelta);

        if (player.getHealth() > 0 && lastDelta > 0.5) {
            divideAmount += 1;
            //heatMapData.add(new Vector2(player.getPlayerBodyX(), player.getPlayerBodyY()));
            heatMap.modify(player.getPlayerBodyX(), player.getPlayerBodyY());
            lastDelta = 0;
        } else if (!calculated) {
            calculated = true;
        }

        */



        camera.update();

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Dodgeball.WORLD_WIDTH, Dodgeball.WORLD_HEIGHT);
        batch.end();

        player.playerMove(delta);

        /*
        int i = 0;
        for (Balls eachBall : ball) {
            eachBall.draw(delta);
            if (eachBall.getX() > Dodgeball.WORLD_WIDTH + 2 || eachBall.getY() > Dodgeball.WORLD_HEIGHT + 2 ||
                    eachBall.getX() < -2 || eachBall.getY() < -2) {
                ballLocator[eachBall.getLocationToUpdateBallLocator()] = 0;
                eachBall.dispose();
                ball[i] = new Balls(world, batch, getPlayerX(), getPlayerY(), ballLocator);
                ballLocator[ball[i].getLocationToUpdateBallLocator()] = 1;
                //Gdx.app.log(getClass().getSimpleName(), "respawning");
            }
            i++;
        }
        */

        ballSpawnTimer += delta;
        if (ballStartCounter < 3) {
            if (ballSpawnTimer > 3) {
                ballsArray.add(new Balls(world, batch, getPlayerX(), getPlayerY(), ballLocator));
                ballLocator[ballsArray.peek().getLocationToUpdateBallLocator()] = 1;
                ballSpawnTimer = 0;
                ballStartCounter++;
            }
        } else if (ballSpawnTimer > 60 && ballStartCounter < 10) {
            ballsArray.add(new Balls(world, batch, getPlayerX(), getPlayerY(), ballLocator));
            ballLocator[ballsArray.peek().getLocationToUpdateBallLocator()] = 1;
            ballSpawnTimer = 0;
            ballStartCounter++;
        }

        int i = 0;
        for (Balls eachBall : ballsArray) {
            eachBall.draw(delta);
            if (eachBall.getX() > Dodgeball.WORLD_WIDTH + 2 || eachBall.getY() > Dodgeball.WORLD_HEIGHT + 2 ||
                    eachBall.getX() < -2 || eachBall.getY() < -2) {
                ballLocator[eachBall.getLocationToUpdateBallLocator()] = 0;
                eachBall.dispose();
                ballsArray.removeIndex(i);
                ballsArray.add(new Balls(world, batch, getPlayerX(), getPlayerY(), ballLocator));
                ballLocator[ballsArray.peek().getLocationToUpdateBallLocator()] = 1;
                //Gdx.app.log(getClass().getSimpleName(), "respawning");
            }
            i++;
        }

        //updateHealth(batch);
        player.drawHealth(delta);

        batch.begin();

        if (player.getHealth() == 0) {
            batch.draw(gameOver,Dodgeball.WORLD_WIDTH / 2 - gameOver.getWidth() / 100 / 2,
                    Dodgeball.WORLD_HEIGHT / 2 - gameOver.getHeight() / 100 / 2,
                    gameOver.getWidth() / 100, gameOver.getHeight() / 100);
            timer.setFreeze();
        }

        batch.end();

        timer.survivalModeTimer();

        debugRenderer.render(world, camera.combined);
        doPhysicsStep(delta);

        // For testing purposes
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            host.setScreen(new Menu(host));
        }
    }

    /*private void updateHealth(SpriteBatch batch) {
        batch.begin();
        for (int i = 0; i < player.getHealth(); i++) {
            batch.draw(health, 0.5f + (0.6f * i), 7.3f, 0.5f, 0.5f);
        }
        batch.end();
    }*/

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
        for (Balls eachBall : ballsArray) {
            eachBall.dispose();
        }
        player.dispose();
        timer.dispose();
        world.destroyBody(walls);
        world.dispose();
        Gdx.app.log(getClass().getSimpleName(), "disposing");
    }
}
