package tamk.fi.polttopallopeli;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ContactDetection implements ContactListener {

    @Override
    public void beginContact(Contact contact) {

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.getUserData() != null
                && fixtureA.getUserData().equals("player")
                && fixtureB.getUserData() != null
                && fixtureB.getUserData().equals("ball")) {

            fixtureB.getBody().setLinearVelocity(5f, 0f);
        }

        if (fixtureB.getUserData() != null
                && fixtureB.getUserData().equals("player")
                && fixtureA.getUserData() != null
                && fixtureA.getUserData().equals("ball")) {

            fixtureA.getBody().setLinearVelocity(5f, 0f);
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
