package tamk.fi.polttopallopeli;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Balls extends Sprite {
    private World world;
    private Body body;
    private Batch batch;
    Texture ball;
    private float xCoordinate;
    private float yCoordinate;
    private float playerX;
    private float playerY;

    public Balls(World world, Batch batch, float playerX, float playerY) {

        this.world = world;
        this.batch = batch;

        ball = new Texture("peruspallo.png");

        this.playerX = playerX;
        this.playerY = playerY;

        float xCoord = randomLocationX();
        float yCoord = randomLocationY();

        setPosition(xCoord, yCoord);

        yCoordinate = Dodgeball.WORLD_HEIGHT / 2;
        xCoordinate = Dodgeball.WORLD_WIDTH / 2;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((randomBodyDefLocationX()),
                (randomBodyDefLocationY()));

        body = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(0.35f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.01f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0.8f;

        // Ball collides with player, walls and other balls (for testing)
        fixtureDef.filter.categoryBits = Dodgeball.OBJECT_BALL;
        fixtureDef.filter.maskBits = Dodgeball.OBJECT_WALL | Dodgeball.OBJECT_PLAYER | Dodgeball.OBJECT_BALL;

        body.createFixture(fixtureDef).setUserData("ball");

        circle.dispose();

        float xImpulse;

        if (xCoordinate > playerX) {
            xImpulse = -0.01f;
        } else {
            xImpulse = 0.01f;
        }



        float yImpulse;

        if (yCoordinate > playerY) {
            yImpulse = -0.01f;
        } else {
            yImpulse = 0.01f;
        }

        body.applyLinearImpulse(xImpulse,yImpulse,0,0,true);
    }

    private float randomBodyDefLocationX() {
        switch (MathUtils.random(1,2)) {
            case 1:
                xCoordinate = Dodgeball.WORLD_WIDTH / 8f;
                return xCoordinate;
            case 2:
                xCoordinate = Dodgeball.WORLD_WIDTH * 0.8f;
                return xCoordinate;
        }
        return xCoordinate;
    }

    private float randomBodyDefLocationY() {
        switch (MathUtils.random(1,2)) {
            case 1:
                yCoordinate = Dodgeball.WORLD_HEIGHT / 8;
                return yCoordinate;
            case 2:
                yCoordinate = Dodgeball.WORLD_HEIGHT * 0.8f;
                return yCoordinate;
        }
        return yCoordinate;
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

    public void draw(float delta) {
        batch.begin();
        batch.draw(ball, body.getPosition().x - ball.getWidth() / 200f,
                body.getPosition().y - ball.getHeight() / 200f, 0.35f, 0.35f,
                0.7f, 0.7f, 1f,
                1f, body.getTransform().getRotation() * MathUtils.radiansToDegrees,
                0, 0, ball.getWidth(), ball.getHeight(), false, false);
        batch.end();
    }
}
