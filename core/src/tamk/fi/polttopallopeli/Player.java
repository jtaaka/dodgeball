package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

/**
 * Class for the player in the game.
 *
 * @author  Juho Taakala <juho.taakala@cs.tamk.fi>
 *          Joni Alanko <joni.alanko@cs.tamk.fi>
 * @since   2018.0222
 * @version 1.0
 */
public class Player extends Sprite {

    /**
     * Defines world.
     */
    private World world;

    /**
     * Defines player's body.
     */
    private Body body;

    /**
     * Defines player's head.
     */
    private Body head;

    /**
     * Defines SpriteBatch for player.
     */
    private SpriteBatch batch;

    /**
     * Defines vector for player movement.
     */
    private Vector2 vector;

    /**
     * Defines player's healths.
     */
    private int health;

    /**
     * Defines texture for healths.
     */
    private Texture healthTexture;

    /**
     * Defines animation for healths.
     */
    private Animation<TextureRegion> healthAnimation;

    /**
     * Defines frame time for healths.
     */
    private float healthFrameTime;

    /**
     * Defines frame duration.
     */
    private float frameDuration = 1/8f;

    /**
     * Defines animation for player.
     */
    private Animation<TextureRegion> playerAnime;

    /**
     * Defines current frame time.
     */
    private float currentFrameTime;

    /**
     * Defines stun texture for player.
     */
    private Texture stunTexture;

    /**
     * Defines stun animation for player.
     */
    private Animation<TextureRegion> stunAnime;

    /**
     * Defines accelerometer Z setting.
     */
    private float tabletAccelerometerSettingZ;

    /**
     * Defines accelerometer Y setting.
     */
    private float tabletAccelerometerSettingY;

    /**
     * Defines if accelerometer X is used.
     */
    private boolean useXaccelerometer = false;

    /**
     * Defines calibration for Z+ axis.
     */
    private float calibrationZPositive;

    /**
     * Defines calibration for Z- axis.
     */
    private float calibrationZNegative;

    /**
     * Defines calibration for X+ axis.
     */
    private float calibrationXPositive;

    /**
     * Defines calibration for X- axis.
     */
    private float calibrationXNegative;

    /**
     * Defines if player won or not.
     */
    public boolean victory;

    /**
     * Constructor for the player.
     *
     * @param world world for the player.
     * @param batch spritebatch for the player.
     */
    public Player(World world, SpriteBatch batch) {
        super(new Texture("animaatio.png"));

        //SettingsPreferences.getSettings();
        //SettingsPreferences.setSettings();

        if (SettingsPreferences.prefs == null) {
            calibrationZPositive = 2f;
            calibrationZNegative = 2f;
            calibrationXPositive = 2f;
            calibrationXNegative = 2f;
        } else {
            calibrationZPositive = 6f - SettingsPreferences.prefs.getFloat("calibrationZPositive");
            calibrationZNegative = 6f - SettingsPreferences.prefs.getFloat("calibrationZNegative");
            calibrationXPositive = 6f - SettingsPreferences.prefs.getFloat("calibrationXPositive");
            calibrationXNegative = 6f - SettingsPreferences.prefs.getFloat("calibrationXNegative");
        }

        healthTexture = new Texture("healths.png");
        invulnerability = 0;

        stunTexture = new Texture("stun.png");

        // animaatio healteille
        TextureRegion[][] temp = TextureRegion.split(healthTexture, healthTexture.getWidth(),
                healthTexture.getHeight() / 4);
        TextureRegion[] healthFrames = transformTo1D(temp);
        healthAnimation = new Animation<TextureRegion>(1/10f, healthFrames);

        temp = TextureRegion.split(getTexture(), getTexture().getWidth() / 9,
                getTexture().getHeight() / 8);
        TextureRegion[] playerFrames = transformTo1D(temp);
        playerAnime = new Animation<TextureRegion>(frameDuration, playerFrames);

        temp = TextureRegion.split(stunTexture, stunTexture.getWidth() / 8,
                stunTexture.getHeight());
        TextureRegion[] stunFrames = transformTo1D(temp);
        stunAnime = new Animation<TextureRegion>(frameDuration, stunFrames);

        setSize(getWidth() / 900f / 3f, getHeight() / 800f / 3f);
        setOriginCenter();

        currentFrameTime = 0;
        healthFrameTime = 0;

        this.world = world;
        this.batch = batch;

        health = 3;
        victory = false;

        vector = new Vector2();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((Dodgeball.WORLD_WIDTH / 2f),
                (Dodgeball.WORLD_HEIGHT / 2f));
        bodyDef.fixedRotation = true;
        bodyDef.linearDamping = 1f;

        body = world.createBody(bodyDef);

        bodyDef.position.set((Dodgeball.WORLD_WIDTH / 2f),
                (Dodgeball.WORLD_HEIGHT / 1.8f));
        head = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth() / 6.5f, getHeight() / 5.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.15f;
        fixtureDef.friction = 1f;
        fixtureDef.filter.categoryBits = Dodgeball.OBJECT_PLAYER;
        fixtureDef.filter.maskBits = Dodgeball.OBJECT_WALL | Dodgeball.OBJECT_BALL;

        body.createFixture(fixtureDef).setUserData(this);

        CircleShape circle = new CircleShape();
        circle.setRadius(0.25f);
        fixtureDef.shape = circle;

        head.createFixture(fixtureDef).setUserData(this);

        WeldJointDef weldJointDef = new WeldJointDef();
        weldJointDef.bodyA = body;
        weldJointDef.bodyB = head;
        weldJointDef.localAnchorA.add(0.02f, getHeight() / 8 * 3.4f);
        world.createJoint(weldJointDef);

        playerCalibration();
        shape.dispose();
        circle.dispose();
    }

    /**
     * Calibrates the player's starting accelerometer position.
     */
    private void playerCalibration() {
        tabletAccelerometerSettingZ = 0;
        tabletAccelerometerSettingY = 0;

        if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
            float accelo = Gdx.input.getAccelerometerZ();
            float acceloX = Gdx.input.getAccelerometerX();

            if (accelo > 9) {
                accelo = 7f;
            } else if (accelo < -9) {
                accelo = -7f;
            }

            if (acceloX > 9) {
                acceloX = 7f;
            } else if (acceloX < -9) {
                acceloX = -7f;
            }

            if (accelo > 5) {
                tabletAccelerometerSettingZ = acceloX;
                useXaccelerometer = true;
            } else {
                tabletAccelerometerSettingZ = accelo;
            }

            tabletAccelerometerSettingY = Gdx.input.getAccelerometerY();
        }
    }

    /**
     * Transforms animation texture to 1D.
     *
     * @param tmp temporary 2D array.
     * @return 1D array of player frames.
     */
    private TextureRegion[] transformTo1D(TextureRegion[][] tmp) {
        TextureRegion [] playerFrames = new TextureRegion[tmp.length * tmp[0].length];

        int index = 0;
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[0].length; j++) {
                playerFrames[index++] = tmp[i][j];
            }
        }

        return playerFrames;
    }

    /**
     * Get player's health amount.
     *
     * @return players health amount.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Decrease player's health amount.
     */
    public void decreaseHealth() {
        health = Math.max(0, health - 1);
    }

    /**
     * Increase player's health amount.
     */
    public void increaseHealth() {
        health = Math.min(3, health + 1);
    }

    /**
     * Get player body x position.
     *
     * @return player body y position.
     */
    public float getPlayerBodyX() {
        return body.getPosition().x;
    }

    /**
     * Get player body y position.
     *
     * @return player body y position.
     */
    public float getPlayerBodyY() {
        return body.getPosition().y;
    }

    /**
     * Determines correct animation frame based on player body velocity.
     *
     * @return returns correct starting point in texture region.
     */
    private float getDirectionalFrameTime() {
        final float DETECTION_THRESHOLD = 0.5f;
        final float FRAME_TIME = frameDuration;
        final float FRAME_COUNT = 9;

        float directionX = body.getLinearVelocity().x;
        float directionY = body.getLinearVelocity().y;

        int currentRow = 6;

        if (directionX < 0 && directionY > 0) {
            currentRow = 3;
        }

        if (directionX > 0 && directionY > 0) {
            currentRow = 5;
        }

        if (directionX > 0 && directionY < 0) {
            currentRow = 7;
        }

        if (directionX < 0 && directionY < 0) {
            currentRow = 1;
        }

        if (directionX < 0 && MathUtils.isZero(directionY, DETECTION_THRESHOLD)) {
            currentRow = 2;
        }

        if (MathUtils.isZero(directionX, DETECTION_THRESHOLD) && directionY > 0) {
            currentRow = 4;
        }

        if (directionX > 0 && MathUtils.isZero(directionY, DETECTION_THRESHOLD)) {
            currentRow = 6;
        }

        if (MathUtils.isZero(directionX, DETECTION_THRESHOLD) && directionY < 0) {
            currentRow = 0;
        }

        // seinät
        if (getPlayerBodyX() >= 12.5f) {
            currentRow = 6;
        }

        if (getPlayerBodyX() <= 0.24f) {
            currentRow = 2;
        }

        if (getPlayerBodyY() >= 7.2f) {
            currentRow = 4;
        }

        if (getPlayerBodyY() <= 0.235f) {
            currentRow = 0;
        }

        return FRAME_TIME * FRAME_COUNT * currentRow;
    }

    /**
     * Used to indicate if player has been hit and for stun animation.
     */
    public boolean hit;
    /**
     * Used to time how long player is invulnerable after being hit.
     */
    float invulnerability;
    /**
     * Used for player movement calculations using Z or X accelerometer.
     */
    float accelZ;
    /**
     * Used for player movement calculations using Y accelerometer.
     */
    float accelY;

    /**
     * Handles player movement and drawing.
     *
     * @param delta is delta time.
     */
    public void playerMove(float delta) {
        float initialFrameTime = getDirectionalFrameTime();

        if (victory || getHealth() == 0) {
            delta = 0;
        }

        batch.begin();

        if (hit) {
            invulnerability += delta;
            if (invulnerability > 1) {
                hit = false;
                invulnerability = 0;
            }
        } else {
            currentFrameTime += delta;
        }

        if (initialFrameTime + currentFrameTime > initialFrameTime + frameDuration * 6f) {
            currentFrameTime = 0;
        }

        TextureRegion currentFrame = playerAnime.getKeyFrame(initialFrameTime + currentFrameTime, true);

        if (!hit) {
            batch.draw(currentFrame, body.getPosition().x - getWidth() / 2f, body.getPosition().y - getHeight() / 4f,
                    getWidth(), getHeight());
        } else {
            currentFrame = stunAnime.getKeyFrame(invulnerability, true);
            batch.draw(currentFrame, body.getPosition().x - getWidth() / 2f, body.getPosition().y - getHeight() / 4f,
                    getWidth(), getHeight());
        }

        vector.set(0, 0);

        // For testing purposes on computer
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            vector.x = -5f * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            vector.x = 5f * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            vector.y = 5f * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            vector.y = -5f * delta;
        }

        accelY = Gdx.input.getAccelerometerY() - tabletAccelerometerSettingY;
        if (useXaccelerometer) {
            accelZ = Gdx.input.getAccelerometerX() - tabletAccelerometerSettingZ;
        } else {
            accelZ = Gdx.input.getAccelerometerZ() - tabletAccelerometerSettingZ;
        }

        if (!MathUtils.isZero(accelY, 0.5f) || !MathUtils.isZero(accelZ, 0.5f)) {

            if (accelY > 0) {
                vector.x = accelY * delta * calibrationXPositive;
            }

            if (accelY < 0) {
                vector.x = accelY * delta * calibrationXNegative;
            }

            if (accelZ > 0) {
                vector.y = accelZ * delta * calibrationZPositive;
            }

            if (accelZ < 0) {
                vector.y = accelZ * delta * calibrationZNegative;
            }
        }

        if (useXaccelerometer) {
            vector.y = -vector.y;
        }

        body.applyForceToCenter(vector, true);
        batch.end();
    }

    /**
     * Gets player body.
     *
     * @return Body body.
     */
    public Body getBody() {
        return body;
    }

    /**
     * Gets player forward and backward leaning.
     *
     * @return correct float value of leaning depending on which accelerometer is in use.
     */
    public float getAccelZ() {
        if (useXaccelerometer) {
            return -accelZ;
        }
        return accelZ;
    }

    /**
     * Gets player sideways leaning.
     *
     * @return float value of current lean.
     */
    public float getAccelY() {
        return accelY;
    }

    private float getHealthFrames() {
        float FRAME_TIME = 1 / 10f;

        int currentRow = 0;

        if (getHealth() == 3) {
            currentRow = 0;
        }

        if (getHealth() == 2) {
            currentRow = 1;
        }

        if (getHealth() == 1) {
            currentRow = 2;
        }

        if (getHealth() == 0) {
            currentRow = 3;
        }

        return FRAME_TIME * currentRow;
    }

    /**
     * Draws player's health animation to screen.
     *
     * @param delta is deltatime.
     */
    public void drawHealth(float delta) {
        float initialFrameTime = getHealthFrames();

        batch.begin();

        healthFrameTime += delta;

        if (initialFrameTime + healthFrameTime > initialFrameTime + 1/10f) {
            healthFrameTime = 0;
        }

        TextureRegion currentFrame = healthAnimation.getKeyFrame(initialFrameTime + healthFrameTime, false);
        batch.draw(currentFrame, 0.5f, 7.3f,
                healthTexture.getWidth() / 100f, healthTexture.getHeight() / 400f);

        batch.end();
    }

    public void dispose() {
        getTexture().dispose();
        world.destroyBody(body);
        healthTexture.dispose();
        stunTexture.dispose();
    }
}