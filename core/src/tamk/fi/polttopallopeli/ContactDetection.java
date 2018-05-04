package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ContactDetection implements ContactListener {
    private Fixture fixtureA;
    private Fixture fixtureB;
    private Dodgeball host;

    public ContactDetection(Dodgeball host) {
        this.host = host;
    }

    @Override
    public void beginContact(Contact contact) {
        fixtureA = contact.getFixtureA();
        fixtureB = contact.getFixtureB();

        if (fixtureA == null || fixtureA.getUserData() == null || fixtureB == null || fixtureB.getUserData() == null) {
            return;
        }

        if (fixtureA.getUserData() instanceof Player || fixtureB.getUserData() instanceof Player) {
            if (fixtureA.getUserData() instanceof Player) {
                Player player = (Player) fixtureA.getUserData();
                Balls ball = (Balls) fixtureB.getUserData();
                if (!player.hit && !player.victory) {
                    Gdx.input.vibrate(200);
                    if (ball.ballIsHealingBall) {
                        player.increaseHealth();
                        ball.healingUsed = true;
                    } else {
                        player.decreaseHealth();
                        player.hit = true;
                    }
                    if (Dodgeball.VOLUME > 0) {
                        host.getManager("hit2.ogg", Sound.class).play(Dodgeball.VOLUME / 2f);
                        host.getManager("hit.ogg", Sound.class).play(Dodgeball.VOLUME);
                    }
                }
            } else {
                Player player = (Player) fixtureB.getUserData();
                Balls ball = (Balls) fixtureA.getUserData();
                if (!player.hit && !player.victory) {
                    Gdx.input.vibrate(200);
                    if (ball.healingBall) {
                        player.increaseHealth();
                        ball.healingUsed = true;
                    } else {
                        player.decreaseHealth();
                        player.hit = true;
                    }
                    if (Dodgeball.VOLUME > 0) {
                        host.getManager("hit2.ogg", Sound.class).play(Dodgeball.VOLUME / 2f);
                        host.getManager("hit.ogg", Sound.class).play(Dodgeball.VOLUME);
                    }
                }
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
