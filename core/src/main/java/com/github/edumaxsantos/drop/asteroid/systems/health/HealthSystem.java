package com.github.edumaxsantos.drop.asteroid.systems.health;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.github.edumaxsantos.drop.asteroid.components.BodyComponent;
import com.github.edumaxsantos.drop.asteroid.components.health.HealthBarComponent;
import com.github.edumaxsantos.drop.asteroid.components.health.HealthComponent;

public class HealthSystem extends IteratingSystem {

    private World world;
    private Array<Entity> entitiesToRemove;

    public HealthSystem(World world) {
        super(Family.all(HealthComponent.class).get());
        entitiesToRemove = new Array<>();
        this.world = world;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        var health = entity.getComponent(HealthComponent.class).health;
        var healthBar = entity.getComponent(HealthBarComponent.class);

        if (health <= 0) {
            entitiesToRemove.add(entity);
        }

        if (healthBar.timeVisible > 0) {
            healthBar.timeVisible -= deltaTime;
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        entitiesToRemove.forEach(entity -> {
            System.out.println("Is dead");
            var body = entity.getComponent(BodyComponent.class).body;
            world.destroyBody(body);
            getEngine().removeEntity(entity);
        });

        entitiesToRemove.clear();
    }

    public void reduceHealth(Entity entity, float amount) {
        var health = entity.getComponent(HealthComponent.class);
        var healthBar = entity.getComponent(HealthBarComponent.class);

        if (health != null) {
            health.health -= amount;

            if (health.health <= 0) {
                entitiesToRemove.add(entity);
            }

            if (healthBar != null) {
                healthBar.timeVisible = 2f;
            }
        }
    }
}


