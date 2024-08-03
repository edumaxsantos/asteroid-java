package com.github.edumaxsantos.drop.asteroid;

import com.badlogic.gdx.physics.box2d.*;

public class MyContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        var bodyA = contact.getFixtureA().getBody();
        var bodyB = contact.getFixtureB().getBody();

        handleAsteroidAndShip(bodyA, bodyB);
        handleMissileAndAsteroid(bodyA, bodyB);
        handleMissileAndStarGold(bodyA, bodyB);
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

    private boolean isMissile(Body body) {
        return body.getUserData() instanceof Missile;
    }

    private boolean isFlyingObject(Body body) {
        return  body.getUserData() instanceof FlyingObject;
    }

    private boolean isStarGold(Body body) {
        return body.getUserData() instanceof StarGold;
    }

    private boolean isShip(Body body) {
        return body.getUserData() instanceof Ship;
    }

    private boolean isAsteroid(Body body) {
        return body.getUserData() instanceof Asteroid;
    }

    private void handleAsteroidAndShip(Body bodyA, Body bodyB) {
        if (isShip(bodyA) && isAsteroid(bodyB)) {
            var ship = (Ship) bodyA.getUserData();
            var asteroid = (Asteroid) bodyB.getUserData();

            asteroidHitsShip(asteroid, ship);
        } else if (isShip(bodyB) && isAsteroid(bodyA)) {
            var ship = (Ship) bodyB.getUserData();
            var asteroid = (Asteroid) bodyA.getUserData();

            asteroidHitsShip(asteroid, ship);
        }
    }

    private void handleMissileAndAsteroid(Body bodyA, Body bodyB) {
        if (isAsteroid(bodyA) && isMissile(bodyB)) {
            var missile = (Missile) bodyB.getUserData();
            var flyingObject = (FlyingObject) bodyA.getUserData();

            missileHitFlyingObject(missile, flyingObject);
        } else if (isAsteroid(bodyB) && isMissile(bodyA)) {
            var missile = (Missile) bodyA.getUserData();
            var flyingObject = (FlyingObject) bodyB.getUserData();

            missileHitFlyingObject(missile, flyingObject);
        }
    }

    private void handleMissileAndStarGold(Body bodyA, Body bodyB) {
        if (isStarGold(bodyA) && isMissile(bodyB)) {
            var starGold = (StarGold) bodyA.getUserData();
            var missile = (Missile) bodyB.getUserData();

            missileHitsStarGold(starGold, missile);
        } else if (isStarGold(bodyB) && isMissile(bodyA)) {
            var starGold = (StarGold) bodyB.getUserData();
            var missile = (Missile) bodyA.getUserData();

            missileHitsStarGold(starGold, missile);
        }
    }

    private void missileHitsStarGold(StarGold starGold, Missile missile) {
        starGold.takeDamage(missile.getDamage());

        if (starGold.isDead()) {
            starGold.destroy();
            ((Ship) missile.getParent()).heal(25f);
        }

        missile.destroy();
    }

    private void asteroidHitsShip(Asteroid asteroid, Ship ship) {
        ship.takeDamage(asteroid.getDamage());

        if (ship.isDead()) {
            ship.destroy();
        }
        asteroid.destroy();
    }

    private void missileHitFlyingObject(Missile missile, FlyingObject flyingObject) {
        flyingObject.takeDamage(missile.getDamage());
        if (flyingObject.isDead()) {
            flyingObject.destroy();
            ((Ship) missile.getParent()).screen.game.increasePoint();
        }

        missile.destroy();
    }
}
