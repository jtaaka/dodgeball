package tamk.fi.polttopallopeli;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ContactDetection implements ContactListener {
    Fixture fixtureA;
    Fixture fixtureB;

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
                player.decreaseHealth();
            } else {
                Player player = (Player) fixtureB.getUserData();
                player.decreaseHealth();
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
