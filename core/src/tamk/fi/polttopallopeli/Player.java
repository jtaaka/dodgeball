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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends Sprite {
    private World world;
    protected Body body;
    private SpriteBatch batch;
    private Vector2 vector;

    private int health;
    private Texture healthTexture;
    private Animation<TextureRegion> healthAnimation;
    private float healthFrameTime;

    private Animation<TextureRegion> playerAnime;
    private float currentFrameTime;

    public Player(World world, SpriteBatch batch) {
        super(new Texture("walk.png"));

        healthTexture = new Texture("healths.png");

        // animaatio healteille
        TextureRegion[][] temp = TextureRegion.split(healthTexture, healthTexture.getWidth(),
                healthTexture.getHeight() / 4);
        TextureRegion[] healthFrames = healthsTo1D(temp);
        healthAnimation = new Animation<TextureRegion>(1/10f, healthFrames);



        TextureRegion[][] tmp = TextureRegion.split(getTexture(), getTexture().getWidth() / 8,
                getTexture().getHeight() / 8);
        TextureRegion[] playerFrames = transformTo1D(tmp);
        playerAnime = new Animation<TextureRegion>(1/10f, playerFrames);

        setSize(getWidth()/8, getHeight()/8);

        currentFrameTime = 0;
        healthFrameTime = 0;
        // Ylempi testauksessa

        this.world = world;
        this.batch = batch;

        //player = new Texture("playertexture.png");
        health = 3;

        vector = new Vector2();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((Dodgeball.WORLD_WIDTH / 2f),
                (Dodgeball.WORLD_HEIGHT / 2f));
        bodyDef.fixedRotation = true;
        bodyDef.linearDamping = 0.5f;

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();

        //shape.setAsBox(player.getWidth()/2 / 100f, player.getHeight() /2 / 100f);
        shape.setAsBox(getWidth()/4 / 100f, getHeight()/2.2f / 100f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.05f;
        fixtureDef.friction = 1f;
        fixtureDef.filter.categoryBits = Dodgeball.OBJECT_PLAYER;
        fixtureDef.filter.maskBits = Dodgeball.OBJECT_WALL | Dodgeball.OBJECT_BALL;

        body.createFixture(fixtureDef).setUserData(this);

        shape.dispose();
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

    private float getDirectionalFrameTime() {
        final float DETECTION_THRESHOLD = 0.5f;
        final float FRAME_TIME = 1 / 10f;
        final float FRAME_COUNT = 8;

        float directionX = body.getLinearVelocity().x;
        float directionY = body.getLinearVelocity().y;

        int currentRow = 6;

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

        return FRAME_TIME * FRAME_COUNT * currentRow;
    }

    public void playerMove(float delta) {
        float initialFrameTime = getDirectionalFrameTime();

        batch.begin();
        //batch.draw(player, body.getPosition().x - player.getWidth() / 200f, body.getPosition().y - player.getHeight() / 200f,
        //        player.getWidth() / 100f, player.getHeight() / 100f);
        currentFrameTime += delta;

        if (initialFrameTime + currentFrameTime > initialFrameTime + 1/10f * 8f) {
            currentFrameTime = 0;
        }

        //if () {
            //Gdx.app.log(getClass().getSimpleName(), "linear velocity: " + body.getLinearVelocity());
        //}
        //moveAnimationFrame();


        TextureRegion currentFrame = playerAnime.getKeyFrame(initialFrameTime + currentFrameTime, true);
        batch.draw(currentFrame, body.getPosition().x - getWidth() / 160f, body.getPosition().y - getHeight() / 220f,
                getWidth() / 80f, getHeight() / 80f);

        vector.set(0, 0);

        // For testing purposes on computer
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            vector.x = -10f * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            vector.x = 10f * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            vector.y = 10f * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            vector.y = -10f * delta;
        }

        // Accelerometer testing for tablet
        float accelY = Gdx.input.getAccelerometerY();
        float accelZ = Gdx.input.getAccelerometerZ();

        if (!MathUtils.isZero(accelY, 0.5f)) {
            vector.x = accelY * delta;
        }

        if (!MathUtils.isZero(accelZ, 0.5f)) {
            vector.y = accelZ * delta;
        }

        body.applyForceToCenter(vector, true);
        batch.end();
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
        //player.dispose();
        super.getTexture().dispose();
        //getTexture().dispose();
        world.destroyBody(body);
        healthTexture.dispose();
        //Gdx.app.log(getClass().getSimpleName(), "disposing");
    }
}
