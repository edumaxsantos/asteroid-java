package com.github.edumaxsantos.drop.asteroid.listeners;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.github.edumaxsantos.drop.asteroid.components.BodyComponent;
import com.github.edumaxsantos.drop.asteroid.components.DamageComponent;
import com.github.edumaxsantos.drop.asteroid.data.EntityData;
import com.github.edumaxsantos.drop.asteroid.enums.ElementType;
import com.github.edumaxsantos.drop.asteroid.systems.health.HealthSystem;

import java.util.Optional;

public class CollisionListener implements ContactListener {

    private final Engine engine;
    private final World world;
    private final Array<Body> bodiesToRemove = new Array<>();

    public CollisionListener(Engine engine, World world) {
        this.engine = engine;
        this.world = world;
    }

    @Override
    public void beginContact(Contact contact) {
        var userDataA = (EntityData) contact.getFixtureA().getBody().getUserData();
        var userDataB = (EntityData) contact.getFixtureB().getBody().getUserData();

        Optional<Entity> optionalPlayer = getEntityOfType(ElementType.PLAYER, userDataA, userDataB);
        Optional<Entity> optionalMissile = getEntityOfType(ElementType.MISSILE, userDataA, userDataB);
        Optional<Entity> optionalAsteroid = getEntityOfType(ElementType.ASTEROID, userDataA, userDataB);

        optionalAsteroid.ifPresent(asteroid -> {
            optionalMissile.ifPresent(missile -> handleAsteroidAndMissile(asteroid, missile));
            optionalPlayer.ifPresent(player -> handleAsteroidAndPlayer(asteroid, player));
        });
    }

    private void handleAsteroidAndMissile(Entity asteroid, Entity missile) {
        var damage = missile.getComponent(DamageComponent.class);

        var healthSystem = engine.getSystem(HealthSystem.class);

        healthSystem.reduceHealth(asteroid, damage.damage);

        var body = missile.getComponent(BodyComponent.class).body;

        bodiesToRemove.add(body);
        engine.removeEntity(missile);
    }

    private void handleAsteroidAndPlayer(Entity asteroid, Entity player) {
        var damage = asteroid.getComponent(DamageComponent.class);

        var healthSystem = engine.getSystem(HealthSystem.class);

        healthSystem.reduceHealth(player, damage.damage);
    }

    private Optional<Entity> getEntityOfType(ElementType type, EntityData entityDataA, EntityData entityDataB) {
        if (entityDataA.is(type)) {
            return Optional.of(entityDataA.entity);
        }
        if (entityDataB.is(type)) {
            return Optional.of(entityDataB.entity);
        }
        return Optional.empty();
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

    public void processPendingDestruction() {
        for(var body: bodiesToRemove) {
            world.destroyBody(body);
        }
        bodiesToRemove.clear();
    }
}
