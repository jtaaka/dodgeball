package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
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
        shootLocation();
        bodyDef.position.set(xCoordinate, yCoordinate);

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
            xImpulse = (playerX - xCoordinate) / 10;
        } else {
            xImpulse = (playerX - xCoordinate) / 10;
        }

        float yImpulse;

        if (yCoordinate > playerY) {
            yImpulse = (playerY - yCoordinate) / 10;
        } else {
            yImpulse = (playerY - yCoordinate) / 10;
        }

        body.applyForceToCenter(xImpulse, yImpulse,true);
    }

    //LaunchBalls metodi tänne tai survivalmodeen. Hallinnoi koska palloja ammutaan ja mihin.

    private void shootLocation() {
        switch (MathUtils.random(1,2)) {
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
        switch (MathUtils.random(1,8)) {
            case 1:
                xCoordinate = -(Dodgeball.WORLD_WIDTH / 8f);
                break;
            case 2:
                xCoordinate = Dodgeball.WORLD_WIDTH * 1.1f;
                break;
            case 3:
                xCoordinate = Dodgeball.WORLD_WIDTH / 2f;
                break;
            case 4:
                xCoordinate = Dodgeball.WORLD_WIDTH / 4f;
                break;
            case 5:
                xCoordinate = Dodgeball.WORLD_WIDTH / 8f;
                break;
            case 6:
                xCoordinate = Dodgeball.WORLD_WIDTH * 0.2f;
                break;
            case 7:
                xCoordinate = Dodgeball.WORLD_WIDTH * 0.4f;
                break;
            case 8:
                xCoordinate = Dodgeball.WORLD_WIDTH * 0.8f;
                break;
        }
    }

    private void randomBodyDefLocationYFocus() {
        switch (MathUtils.random(1,8)) {
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
        switch (MathUtils.random(1,2)) {
            case 1:
                xCoordinate = -(Dodgeball.WORLD_WIDTH / 8f);
                break;
            case 2:
                xCoordinate = Dodgeball.WORLD_WIDTH * 1.1f;
                break;
        }
    }

    private void randomBodyDefLocationY() {
        switch (MathUtils.random(1,2)) {
            case 1:
                yCoordinate = -(Dodgeball.WORLD_HEIGHT / 8);
                break;
            case 2:
                yCoordinate = Dodgeball.WORLD_HEIGHT * 1.1f;
                break;
        }
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
        setPosition(body.getPosition().x, body.getPosition().y);
    }

    public void dispose() {
        ball.dispose();
        world.destroyBody(body);
    }
}
