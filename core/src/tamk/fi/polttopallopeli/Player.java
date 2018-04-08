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

public class Player extends Sprite {
    private World world;
    protected Body body;
    protected Body head;
    private SpriteBatch batch;
    private Vector2 vector;

    private int health;
    private Texture healthTexture;
    private Animation<TextureRegion> healthAnimation;
    private float healthFrameTime;

    private Animation<TextureRegion> playerAnime;
    private float currentFrameTime;

    private float tabletAccelerometerSettingZ;
    private float tabletAccelerometerSettingY;

    private float calibrationZPositive;
    private float calibrationZNegative;
    private float calibrationXPositive;
    private float calibrationXNegative;

    public boolean victory;

    public Player(World world, SpriteBatch batch) {
        //super(new Texture("walk.png"));
        super(new Texture("hahmo.png"));

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
            Gdx.app.log(getClass().getSimpleName(), "calibrationZPositive: " + calibrationZPositive);
        }

        healthTexture = new Texture("healths.png");
        invulnerability = 0;

        // animaatio healteille
        TextureRegion[][] temp = TextureRegion.split(healthTexture, healthTexture.getWidth(),
                healthTexture.getHeight() / 4);
        TextureRegion[] healthFrames = healthsTo1D(temp);
        healthAnimation = new Animation<TextureRegion>(1/10f, healthFrames);

/*
        TextureRegion[][] tmp = TextureRegion.split(getTexture(), getTexture().getWidth() / 8,
                getTexture().getHeight() / 8);
        TextureRegion[] playerFrames = transformTo1D(tmp);
        playerAnime = new Animation<TextureRegion>(1/10f, playerFrames);
*/
        //setSize(getWidth() / 800f, getHeight() / 800f);
        setSize(getWidth() / 200f, getHeight() / 200f);
        setOriginCenter();

        currentFrameTime = 0;
        healthFrameTime = 0;
        // Ylempi testauksessa

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
        bodyDef.linearDamping = 1f; //0.5f OG

        body = world.createBody(bodyDef);

        bodyDef.position.set((Dodgeball.WORLD_WIDTH / 2f),
                (Dodgeball.WORLD_HEIGHT / 1.8f));
        head = world.createBody(bodyDef);


        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth() / 8.5f, getHeight() / 5.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.15f; // 0.05f OG
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
        weldJointDef.localAnchorA.add(0.02f, getHeight() / 8 * 3.5f);
        world.createJoint(weldJointDef);

        //Kalibroi lähtöasennon, ei vaikuta desktopilla
        tabletAccelerometerSettingZ = 0;
        tabletAccelerometerSettingY = 0;
        if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
            tabletAccelerometerSettingZ = Gdx.input.getAccelerometerZ();
            tabletAccelerometerSettingY = Gdx.input.getAccelerometerY();
        }

        shape.dispose();
        circle.dispose();
    }

    private TextureRegion[] transformTo1D(TextureRegion[][] tmp) {
        TextureRegion [] playerFrames = new TextureRegion[tmp.length * tmp[0].length];

        int index = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                playerFrames[index++] = tmp[i][j];
            }
        }

        return playerFrames;
    }

    public int getHealth() {
        return health;
    }

    public void decreaseHealth() {
        health = Math.max(0, health - 1);
    }

    public float getPlayerBodyX() {
        return body.getPosition().x;
    }

    public float getPlayerBodyY() {
        return body.getPosition().y;
    }

    int lastRow = 6;

    private float getDirectionalFrameTime() {
        final float DETECTION_THRESHOLD = 0.5f;
        final float FRAME_TIME = 1 / 10f;
        final float FRAME_COUNT = 8;

        float directionX = body.getLinearVelocity().x;
        float directionY = body.getLinearVelocity().y;

        int currentRow = lastRow;

        if (directionX < 0 && directionY > 0) {
            currentRow = 1;
        }

        if (directionX > 0 && directionY > 0) {
            currentRow = 3;
        }

        if (directionX > 0 && directionY < 0) {
            currentRow = 5;
        }

        if (directionX < 0 && directionY < 0) {
            currentRow = 7;
        }

        if (directionX < 0 && MathUtils.isZero(directionY, DETECTION_THRESHOLD)) {
            currentRow = 0;
        }

        if (MathUtils.isZero(directionX, DETECTION_THRESHOLD) && directionY > 0) {
            currentRow = 2;
        }

        if (directionX > 0 && MathUtils.isZero(directionY, DETECTION_THRESHOLD)) {
            currentRow = 4;
        }

        if (MathUtils.isZero(directionX, DETECTION_THRESHOLD) && directionY < 0) {
            currentRow = 6;
        }

        // seinät
        if (getPlayerBodyX() >= 12.6f) {
            currentRow = 4;
        }

        if (getPlayerBodyX() <= 0.2f) {
            currentRow = 0;
        }

        if (getPlayerBodyY() >= 7.6f) {
            currentRow = 2;
        }

        if (getPlayerBodyY() <= 0.4f) {
            currentRow = 6;
        }

        //lastRow = currentRow;

        return FRAME_TIME * FRAME_COUNT * currentRow;
    }

    boolean hit;
    float invulnerability;
    float accelZ;
    float accelY;
    boolean faceRight = true;

    public void playerMove(float delta) {
        //float initialFrameTime = getDirectionalFrameTime();

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

        /*
        if (initialFrameTime + currentFrameTime > initialFrameTime + 1/10f * 8f) {
            currentFrameTime = 0;
        }
        */

        //TextureRegion currentFrame = playerAnime.getKeyFrame(initialFrameTime + currentFrameTime, true);

        //batch.draw(currentFrame, body.getPosition().x - getWidth() / 160f, body.getPosition().y - getHeight() / 220f,
        //       getWidth() / 80f, getHeight() / 80f);
        // ^^ OLD? ^^

        /*
        batch.draw(currentFrame, body.getPosition().x - getWidth() / 2f, body.getPosition().y - getHeight() / 2.4f,
                getWidth(), getHeight());
                */

        setPosition(body.getPosition().x - getWidth() / 2.3f,body.getPosition().y - getHeight() / 4);


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

        // Accelerometer testing for tablet
        accelY = Gdx.input.getAccelerometerY() - tabletAccelerometerSettingY;
        accelZ = Gdx.input.getAccelerometerZ() - tabletAccelerometerSettingZ; //ei vaikuta Desktopilla

        float directionX = body.getLinearVelocity().x;

        if (directionX < 0) {
            if (!faceRight) {
                flip(true, false);
                faceRight = true;
            }
        } else {
            if (faceRight) {
                flip(true, false);
                faceRight = false;
            }
            setPosition(body.getPosition().x - getWidth() / 1.9f,body.getPosition().y - getHeight() / 4);
        }

        draw(batch);

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

        body.applyForceToCenter(vector, true);
        batch.end();
    }



    public float getAccelZ() {
        return accelZ;
    }

    public float getAccelY() {
        return accelY;
    }

    private TextureRegion[] healthsTo1D(TextureRegion[][] tmp) {
        TextureRegion [] healthFrames = new TextureRegion[tmp.length * tmp[0].length];

        int index = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 1; j++) {
                healthFrames[index++] = tmp[i][j];
            }
        }

        return healthFrames;
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
        //Gdx.app.log(getClass().getSimpleName(), "disposing");
    }
}