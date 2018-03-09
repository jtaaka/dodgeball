package tamk.fi.polttopallopeli;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Joni Alanko on 09/03/2018.
 */

public class Balls extends Sprite {
    private World world;
    private Body body;
    private Batch batch;
    Texture ball;

    public Balls(World world, Batch batch) {

        this.world = world;
        this.batch = batch;

        ball = new Texture("pallo.png");

        setPosition(Dodgeball.WINDOW_WIDTH / 2 - getWidth() / 2,
                Dodgeball.WINDOW_HEIGHT / 2 - getHeight()/ 2);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((Dodgeball.WORLD_WIDTH / 2f),
                (Dodgeball.WORLD_HEIGHT / 2f));



        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();

        shape.setAsBox(ball.getWidth() / 2 / 100f,
                ball.getHeight() / 2 / 100f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.2f;
        fixtureDef.friction = 1f;

        body.createFixture(fixtureDef);
        body.setUserData(this);

        shape.dispose();
    }

    public void draw(float delta) {
        batch.begin();
        batch.draw(ball, body.getPosition().x - ball.getWidth() / 200f, body.getPosition().y - ball.getHeight() / 200f,
                ball.getWidth() / 100f, ball.getHeight() / 100f);
        batch.end();
    }
}
