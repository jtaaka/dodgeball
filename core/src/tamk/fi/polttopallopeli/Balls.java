package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Defines a ball and its drawing.
 *
 * @author  Joni Alanko <joni.alanko@cs.tamk.fi>
 *          Juho Taakala <juho.taakala@cs.tamk.fi>
 * @since   2018.0222
 * @version 1.0
 */
public class Balls extends Sprite {

    /**
     * Defines world.
     */
    private World world;

    /**
     * Defines ball body.
     */
    private Body body;

    /**
     * Defines player body.
     */
    private Body playerBody;

    /**
     * Defines Spritebatch.
     */
    private Batch batch;

    /**
     * Defines ball texture.
     */
    private Texture ball;

    /**
     * Defines FixtureDef for a ball.
     */
    private FixtureDef fixtureDef;

    /**
     * Ball spawning location.
     */
    private float xCoordinate;

    /**
     * Ball spawning location.
     */
    private float yCoordinate;

    /**
     * Current player location.
     */
    private float playerX;

    /**
     * Current player location.
     */
    private float playerY;

    /**
     * Ball spawning point that can be logged.
     */
    private int ballLocation;

    /**
     * Force given to a ball.
     */
    private float xForce;

    /**
     * Force given to a ball.
     */
    private float yForce;

    /**
     * Defines if they are used.
     */
    private boolean acceleratingBall;

    /**
     * Defines if they are used.
     */
    private boolean targetingBall;

    /**
     * Defines if they are used.
     */
    private boolean fastball;

    /**
     * Defines if they are used.
     */
    boolean healingBall;

    /**
     * Defines if the ball is a healingBall. Defined for ContactDetection.
     */
    boolean ballIsHealingBall = false;

    /**
     * Defines if the healing is used. Defined for Dispose.
     */
    public boolean healingUsed = false;

    /**
     * Defines if accelerating balls functionality is done.
     */
    private boolean acceleratingBallAction = false;

    /**
     * Overloads constructor.
     *
     * @param world current world state.
     * @param batch Spritebatch.
     * @param playerBody current player state.
     * @param ballLocator array that keeps track of available spawnpoints.
     * @param ACCELERATING_BALL boolean, are they used.
     * @param TARGETING_BALL boolean, are they used.
     * @param FASTBALL boolean, are they used.
     * @param HEALINGBALL boolean, are they used.
     */
    public Balls(World world, Batch batch, Body playerBody, int[] ballLocator,boolean ACCELERATING_BALL,
                 boolean TARGETING_BALL,boolean FASTBALL, boolean HEALINGBALL) {
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
        healingBall = HEALINGBALL;

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

        fixtureDef.filter.categoryBits = Dodgeball.OBJECT_BALL;
        fixtureDef.filter.maskBits = Dodgeball.OBJECT_WALL | Dodgeball.OBJECT_PLAYER | Dodgeball.OBJECT_BALL;

        body.createFixture(fixtureDef).setUserData(this);

        circle.dispose();

        body.applyTorque(0.005f, true);
        body.applyForceToCenter(xForce, yForce,true);
    }

    /**
     * Defines ball type randomly.
     */
    private void ballType () {
        switch (MathUtils.random(1,40)) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                if (fastball) {
                    xForce = (playerX - xCoordinate) / 30;
                    yForce = (playerY - yCoordinate) / 30;
                    Gdx.app.log(getClass().getSimpleName(), "Fastball");
                    super.setColor(Color.RED);
                    break;
                }
            case 10:
            case 11:
                if (acceleratingBall) {
                    xForce = (playerX - xCoordinate) / 50;
                    yForce = (playerY - yCoordinate) / 50;
                    acceleratingBallAction = true;
                    ball = new Texture("kiihtyvapallo2.png");
                    setTexture(ball);
                    Gdx.app.log(getClass().getSimpleName(), "Accelerating ball");
                    break;
                }
            case 15:
                if (healingBall) {
                    xForce = (playerX - xCoordinate) / 50;
                    yForce = (playerY - yCoordinate) / 50;
                    ball = new Texture("elamapallo.png");
                    setTexture(ball);
                    ballIsHealingBall = true;
                    break;
                }
            case 20:
            case 21:
            case 22:
            case 23:
                if (targetingBall) {
                    setColor(Color.CHARTREUSE);
                    xForce = (playerX + playerBody.getLinearVelocity().x * 2f - xCoordinate) / 30;
                    yForce = (playerY + playerBody.getLinearVelocity().y * 2f - yCoordinate) / 30;
                    Gdx.app.log(getClass().getSimpleName(), "Targeting ball");
                    break;
                }
            default:
                xForce = (playerX - xCoordinate) / 50;
                yForce = (playerY - yCoordinate) / 50;
                break;
        }
    }

    /**
     * Gets ball spawning point.
     *
     * @return int ballLocation.
     */
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

    /**
     * Hides balls from screen.
     */
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

    private float accelWait;

    /**
     * Draws ball and checks accelerating ball action.
     *
     * @param delta is deltaTime
     */
    public void draw(float delta) {
        accelWait += delta;
        if (acceleratingBallAction && accelWait > 1.8f) {
            body.applyForceToCenter((playerX - xCoordinate) / 30, (playerY - yCoordinate) / 30, true);
            acceleratingBallAction = false;
        }

        batch.begin();

        setRotation(body.getTransform().getRotation() * MathUtils.radiansToDegrees);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        super.draw(batch);
        batch.end();
    }

    public void dispose() {
        getTexture().dispose();
        world.destroyBody(body);
    }
}
