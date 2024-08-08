package com.github.edumaxsantos.drop.asteroid.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.edumaxsantos.drop.asteroid.components.SpriteComponent;

public class RenderingSystem extends IteratingSystem {
    private final SpriteBatch batch;

    public RenderingSystem(SpriteBatch batch) {
        super(Family.all(SpriteComponent.class).get());

        this.batch = batch;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        var spriteComponent = entity.getComponent(SpriteComponent.class);

        batch.begin();
        spriteComponent.getSprite().draw(batch);
        batch.end();
    }
}
