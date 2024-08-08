package com.github.edumaxsantos.drop.asteroid.listeners;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.github.edumaxsantos.drop.asteroid.components.health.HealthComponent;
import com.github.edumaxsantos.drop.asteroid.data.EntityData;
import com.github.edumaxsantos.drop.asteroid.enums.ElementType;
import com.github.edumaxsantos.drop.asteroid.systems.health.HealthSystem;

public class CollisionListener implements ContactListener {

    private Engine engine;

    public CollisionListener(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void beginContact(Contact contact) {
        var userDataA = (EntityData) contact.getFixtureA().getBody().getUserData();
        var userDataB = (EntityData) contact.getFixtureB().getBody().getUserData();

        Entity player;

        if (isPlayer(userDataA)) {
            player = (Entity) userDataA.entity;
            var health = player.getComponent(HealthComponent.class);

            var healthSystem = engine.getSystem(HealthSystem.class);

            healthSystem.reduceHealth(player, 75);

        }
    }

    private boolean isPlayer(EntityData entityData) {
        return entityData.type == ElementType.PLAYER;
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
