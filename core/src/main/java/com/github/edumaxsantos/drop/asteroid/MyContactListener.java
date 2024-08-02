package com.github.edumaxsantos.drop.asteroid;

import com.badlogic.gdx.physics.box2d.*;

public class MyContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        var bodyA = contact.getFixtureA().getBody();
        var bodyB = contact.getFixtureB().getBody();



        if (bodyA.getUserData() instanceof Ship ship && bodyB.getUserData() instanceof Asteroid asteroid) {
            ship.takeDamage(asteroid.getDamage());

            if (ship.isDead()) {
                ship.destroy();
            }
            asteroid.destroy();
        }

        missileHitAsteroid(bodyA, bodyB);
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        //System.out.println("Got preSolve");
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private void missileHitAsteroid(Body bodyA, Body bodyB) {
        if (bodyA.getUserData() instanceof Asteroid asteroid && bodyB.getUserData() instanceof Missile missile) {
            asteroid.takeDamage(missile.getDamage());
            if (asteroid.isDead()) {
                asteroid.destroy();
                ((Ship) missile.getParent()).screen.game.increasePoint();
            }

            missile.destroy();
        }else if (bodyB.getUserData() instanceof Asteroid asteroid && bodyA.getUserData() instanceof Missile missile) {
            asteroid.takeDamage(missile.getDamage());
            if (asteroid.isDead()) {
                asteroid.destroy();
                ((Ship) missile.getParent()).screen.game.increasePoint();
            }

            missile.destroy();
        }
    }
}
