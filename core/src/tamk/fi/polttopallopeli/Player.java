package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player {
    private World world;
    private Body body;
    private SpriteBatch batch;
    private Vector2 vector;
    Texture player;

    public Player(World world, SpriteBatch batch) {
        this.world = world;
        this.batch = batch;

        player = new Texture("playertexture.png");

        vector = new Vector2();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((Dodgeball.WORLD_WIDTH / 2f),
                (Dodgeball.WORLD_HEIGHT / 2f));
        bodyDef.fixedRotation = true;
        bodyDef.linearDamping = 0.5f;

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();

        shape.setAsBox(player.getWidth()/2 / 100f, player.getHeight()
                /2 / 100f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.05f;
        fixtureDef.friction = 1f;
        fixtureDef.filter.categoryBits = Dodgeball.OBJECT_PLAYER;
        fixtureDef.filter.maskBits = Dodgeball.OBJECT_WALL | Dodgeball.OBJECT_BALL;

        body.createFixture(fixtureDef).setUserData("player");

        shape.dispose();
    }

    public void playerMove(float delta) {

        batch.begin();
        batch.draw(player, body.getPosition().x - player.getWidth() / 200f, body.getPosition().y - player.getHeight() / 200f,
                player.getWidth() / 100f, player.getHeight() / 100f);

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
        //float accelX = Gdx.input.getAccelerometerX();
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
}
