package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    public Player(World world) {
        this.world = world;

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
        fixtureDef.density = 0.3f;
        fixtureDef.friction = 1f;

        body.createFixture(fixtureDef);
        body.setUserData(sprite);
    }

    public void playerMove() {

        float delta = Gdx.graphics.getDeltaTime();

        batch.begin();
        sprite.draw(batch);

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            body.applyForceToCenter(new Vector2(-100f * delta, 0), true);

            sprite.setPosition((body.getPosition().x * 100f) - sprite.getWidth() / 2,
                    (body.getPosition().y * 100f) - sprite.getHeight() / 2);


        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            body.applyForceToCenter(new Vector2(100f * delta, 0), true);

            sprite.setPosition((body.getPosition().x * 100f) - sprite.getWidth() / 2,
                    (body.getPosition().y * 100f) - sprite.getHeight() / 2);
        }

        batch.end();
    }
}
