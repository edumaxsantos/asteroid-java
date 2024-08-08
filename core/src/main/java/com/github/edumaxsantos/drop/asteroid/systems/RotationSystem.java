package com.github.edumaxsantos.drop.asteroid.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.github.edumaxsantos.drop.asteroid.components.BodyComponent;
import com.github.edumaxsantos.drop.asteroid.components.PositionComponent;
import com.github.edumaxsantos.drop.asteroid.components.RotationComponent;
import com.github.edumaxsantos.drop.asteroid.components.SpriteComponent;

public class RotationSystem extends IteratingSystem {

    public RotationSystem() {
        super(Family.all(RotationComponent.class, SpriteComponent.class, PositionComponent.class, BodyComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        var rotation = entity.getComponent(RotationComponent.class);
        var sprite = entity.getComponent(SpriteComponent.class);
        var bodyComponent = entity.getComponent(BodyComponent.class);
        var position = entity.getComponent(PositionComponent.class);

        var angle = rotation.angle;
        var body = bodyComponent.body;

        sprite.getSprite().setPosition(position.getX() - sprite.getSprite().getWidth() / 2, position.getY() - sprite.getSprite().getHeight() / 2);

        sprite.getSprite().setRotation(angle);

        body.setTransform(position.position, angle * MathUtils.degreesToRadians);
    }
}
