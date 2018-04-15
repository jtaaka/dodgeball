package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Balls extends Sprite {
    private World world;
    private Body body;
    private Body playerBody;
    private Batch batch;
    private Texture ball;
    FixtureDef fixtureDef;

    private float xCoordinate;
    private float yCoordinate;
    private float playerX;
    private float playerY;
    private int ballLocation;
    float xForce;
    float yForce;

    boolean acceleratingBall;
    boolean targetingBall;
    boolean fastball;
    private boolean acceleratingBallAction = false;
    private boolean bouncingBallAction = false;

    public Balls(World world, Batch batch, Body playerBody, int[] ballLocator,boolean ACCELERATING_BALL,boolean TARGETING_BALL,boolean FASTBALL) {
        super(new Texture("ball.png"));
        setSize(getWidth() / 140f, getHeight() / 140f);
        setOriginCenter();
        this.world = world;
        this.batch = batch;

        this.playerBody = playerBody;
        playerX = playerBody.getPosition().x;
        playerY = playerBody.getPosition().y;

        acceleratingBall = ACCELERATING_BALL;
        targetingBall = TARGETING_BALL;
        fastball = FASTBALL;

        float xCoord = randomLocationX();
        float yCoord = randomLocationY();

        setPosition(xCoord, yCoord);

        yCoordinate = Dodgeball.WORLD_HEIGHT / 2;
        xCoordinate = Dodgeball.WORLD_WIDTH / 2;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        boolean loop = true;
        while (loop) {
            shootLocation();
            if (ballLocator[ballLocation] == 0) {
                loop = false;
            }
        }
        //Gdx.app.log(getClass().getSimpleName(), " " + ballLocation);

        bodyDef.position.set(xCoordinate, yCoordinate);

        body = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(0.25f);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.01f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0.8f;

        ballType();
        // Ball collides with player and other balls (for testing)
        fixtureDef.filter.categoryBits = Dodgeball.OBJECT_BALL;
        fixtureDef.filter.maskBits = Dodgeball.OBJECT_WALL | Dodgeball.OBJECT_PLAYER | Dodgeball.OBJECT_BALL;

        body.createFixture(fixtureDef).setUserData("ball");

        circle.dispose();

        body.applyTorque(0.005f, true);
        body.applyForceToCenter(xForce, yForce,true);
    }

    //LaunchBalls metodi tänne tai survivalmodeen. Hallinnoi koska palloja ammutaan ja mihin.

    private void ballType () {
        switch (MathUtils.random(1,20)) {
            case 1:
            case 2:
            case 3:
            case 4:
                if (fastball) {
                    xForce = (playerX - xCoordinate) / 30;
                    yForce = (playerY - yCoordinate) / 30;
                    Gdx.app.log(getClass().getSimpleName(), "Fastball");
                    super.setColor(Color.RED);
                    //ball = new Texture("peruspallo2.png");
                    //setTexture(ball);
                    break;
                }
            case 10:
                if (acceleratingBall) {
                    //setColor(Color.BLACK);
                    xForce = (playerX - xCoordinate) / 50;
                    yForce = (playerY - yCoordinate) / 50;
                    acceleratingBallAction = true;
                    ball = new Texture("kiihtyvapallo2.png");
                    setTexture(ball);
                    Gdx.app.log(getClass().getSimpleName(), "Accelerating ball");
                    break;
                }
            case 20:
                if (targetingBall) {
                    //bouncingBallAction = true;
                    setColor(Color.CHARTREUSE);
                    xForce = (playerX + playerBody.getLinearVelocity().x * 2f - xCoordinate) / 30;
                    yForce = (playerY + playerBody.getLinearVelocity().y * 2f - yCoordinate) / 30;
                    Gdx.app.log(getClass().getSimpleName(), "Targeting ball");
                    break;
                }
            default:
                xForce = (playerX - xCoordinate) / 50;
                yForce = (playerY - yCoordinate) / 50;
                //setColor(Color.RED);
                break;
        }
    }

    public int getLocationToUpdateBallLocator() {
        return ballLocation;
    }

    private void shootLocation() {
        int random = MathUtils.random(1,2);
        ballLocation = 32 - random * 16;
        switch (random) {
            case 1:
                randomBodyDefLocationXFocus();
                randomBodyDefLocationY();
                break;
            case 2:
                randomBodyDefLocationYFocus();
                randomBodyDefLocationX();
                break;
        }
    }

    private void randomBodyDefLocationXFocus() {
        int random = MathUtils.random(1,6);
        ballLocation += random;
        switch (random) {
            case 1:
                xCoordinate = Dodgeball.WORLD_WIDTH / 2f;
                break;
            case 2:
                xCoordinate = Dodgeball.WORLD_WIDTH / 4f;
                break;
            case 3:
                xCoordinate = Dodgeball.WORLD_WIDTH / 8f;
                break;
            case 4:
                xCoordinate = Dodgeball.WORLD_WIDTH * 0.2f;
                break;
            case 5:
                xCoordinate = Dodgeball.WORLD_WIDTH * 0.4f;
                break;
            case 6:
                xCoordinate = Dodgeball.WORLD_WIDTH * 0.8f;
                break;
        }
    }

    private void randomBodyDefLocationYFocus() {
        int random = MathUtils.random(1,8);
        ballLocation += random;
        switch (random) {
            case 1:
                yCoordinate = -(Dodgeball.WORLD_HEIGHT / 8f);
                break;
            case 2:
                yCoordinate = Dodgeball.WORLD_HEIGHT * 1.1f;
                break;
            case 3:
                yCoordinate = Dodgeball.WORLD_HEIGHT / 2f;
                break;
            case 4:
                yCoordinate = Dodgeball.WORLD_HEIGHT / 4f;
                break;
            case 5:
                yCoordinate = Dodgeball.WORLD_HEIGHT / 8f;
                break;
            case 6:
                yCoordinate = Dodgeball.WORLD_HEIGHT * 0.2f;
                break;
            case 7:
                yCoordinate = Dodgeball.WORLD_HEIGHT * 0.4f;
                break;
            case 8:
                yCoordinate = Dodgeball.WORLD_HEIGHT * 0.8f;
                break;
        }
    }

    private void randomBodyDefLocationX() {
        int random = MathUtils.random(1,2);
        ballLocation += random * 8 - 8 - 1;
        switch (random) {
            case 1:
                xCoordinate = -(Dodgeball.WORLD_WIDTH / 8f);
                break;
            case 2:
                xCoordinate = Dodgeball.WORLD_WIDTH * 1.1f;
                break;
        }
    }

    private void randomBodyDefLocationY() {
        int random = MathUtils.random(1,2);
        ballLocation += random * 8 - 8 - 1;
        switch (random) {
            case 1:
                yCoordinate = -(Dodgeball.WORLD_HEIGHT / 8);
                break;
            case 2:
                yCoordinate = Dodgeball.WORLD_HEIGHT * 1.1f;
                break;
        }
    }

    public void outOfSightOutOfMind() {
        setPosition(-4f, -4f);
    }

    private float randomLocationY() {
        switch (MathUtils.random(1,2)) {
            case 1:
                return Dodgeball.WINDOW_HEIGHT / 8 - getHeight() / 2;
            case 2:
                return Dodgeball.WINDOW_HEIGHT * 0.8f - getHeight() / 2;
        }
        return Dodgeball.WINDOW_HEIGHT / 2 - getHeight() / 2;
    }

    private float randomLocationX() {
        switch (MathUtils.random(1,2)) {
            case 1:
                return Dodgeball.WINDOW_WIDTH / 8 - getWidth() / 2;
            case 2:
                return Dodgeball.WINDOW_WIDTH * 0.8f - getWidth() / 2;
        }
        return Dodgeball.WINDOW_WIDTH / 2 - getWidth() / 2;
    }
    float accelWait;
    int bounces;
    float bounce;

    public void draw(float delta) {
        accelWait += delta;
        if (acceleratingBallAction && accelWait > 1.8f) {
            body.applyForceToCenter((playerX - xCoordinate) / 30, (playerY - yCoordinate) / 30, true);
            acceleratingBallAction = false;
        }

        /*
        if (bouncingBallAction && bounces < 5 && accelWait > 1.8f) {
            bounce += delta;
            if ((body.getPosition().x < 0 || body.getPosition().x > Dodgeball.WORLD_WIDTH || body.getPosition().y < 0 || body.getPosition().y > Dodgeball.WORLD_HEIGHT) && bounce > 0.5f) {
                xForce = (Dodgeball.WORLD_WIDTH / 2 - body.getPosition().x) / 20;
                yForce = (Dodgeball.WORLD_HEIGHT / 2 - body.getPosition().y) / 20;
                body.applyForceToCenter(xForce, yForce,true);
                bounces++;
                bounce = 0;
            }
        }
        */

        batch.begin();
        /*
        batch.draw(getTexture(), body.getPosition().x - getWidth() / 275f,
                body.getPosition().y - getHeight() / 275f, 0.25f, 0.25f,
                0.5f, 0.5f, 1f,
                1f, body.getTransform().getRotation() * MathUtils.radiansToDegrees,
                0, 0, getTexture().getWidth(), getTexture().getHeight(), false, false);
                */
        setRotation(body.getTransform().getRotation() * MathUtils.radiansToDegrees);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        super.draw(batch);
        batch.end();
    }

    public void dispose() {
        getTexture().dispose();
        world.destroyBody(body);
        //Gdx.app.log(getClass().getSimpleName(), "disposing");
    }
}
