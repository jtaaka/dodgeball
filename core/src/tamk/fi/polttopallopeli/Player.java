package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
    private Sprite sprite;
    private Body body;
    private SpriteBatch batch;
    private Vector2 vector;

    public Player(World world) {
        this.world = world;

        vector = new Vector2();

        batch = new SpriteBatch();
        sprite = new Sprite(new Texture("playertexture.png"));
        sprite.setPosition(Dodgeball.WINDOW_WIDTH / 2 - sprite.getWidth() / 2,
                Dodgeball.WINDOW_HEIGHT / 2 - sprite.getHeight()/ 2);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((sprite.getX() + sprite.getWidth()/2) / 100f,
                (sprite.getY() + sprite.getHeight()/2) / 100f);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();

        shape.setAsBox(sprite.getWidth() / 2 / 100f,
                sprite.getHeight() / 2 / 100f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.2f;
        fixtureDef.friction = 1f;

        body.createFixture(fixtureDef);
        body.setUserData(sprite);

        shape.dispose();
    }

    public void playerMove() {

        float delta = Gdx.graphics.getDeltaTime();

        batch.begin();
        sprite.draw(batch);
        vector.set(0, 0);

        // For testing purposes on computer
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            vector.x = -10f * delta;

        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            vector.x = 10f * delta;

        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            vector.y = 10f * delta;

        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            vector.y = -10f * delta;

        }

        // Accelerometer testing for tablet
        //float accelX = Gdx.input.getAccelerometerX();
        float accelY = Gdx.input.getAccelerometerY();
        float accelZ = Gdx.input.getAccelerometerZ();

        if (!MathUtils.isZero(accelY, 0.5f)) {
            vector.x = accelY * delta;

        } else if (!MathUtils.isZero(accelZ, 0.5f)) {
            vector.y = accelZ * delta;
        }

        body.applyForceToCenter(vector, true);

        sprite.setPosition((body.getPosition().x * 100f) - sprite.getWidth() / 2,
                (body.getPosition().y * 100f) - sprite.getHeight() / 2);

        batch.end();
    }
}
