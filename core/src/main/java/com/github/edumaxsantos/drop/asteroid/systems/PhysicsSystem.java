package com.github.edumaxsantos.drop.asteroid.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.github.edumaxsantos.drop.asteroid.components.BodyComponent;
import com.github.edumaxsantos.drop.asteroid.components.PositionComponent;
import com.github.edumaxsantos.drop.asteroid.components.SpriteComponent;

public class PhysicsSystem extends IteratingSystem {
    private static final float TIME_STEP = 1 / 60f;
    private final World world;
    private float accumulator = 0f;

    public PhysicsSystem(World world) {
        super(Family.all(BodyComponent.class, PositionComponent.class).get());
        this.world = world;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        accumulator += deltaTime;

        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        var bodyComponent = entity.getComponent(BodyComponent.class);
        var positionComponent = entity.getComponent(PositionComponent.class);
        var spriteComponent = entity.getComponent(SpriteComponent.class);


    }
}
