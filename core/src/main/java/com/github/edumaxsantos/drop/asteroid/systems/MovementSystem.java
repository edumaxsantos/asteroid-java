package com.github.edumaxsantos.drop.asteroid.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.github.edumaxsantos.drop.asteroid.components.BodyComponent;
import com.github.edumaxsantos.drop.asteroid.components.PositionComponent;
import com.github.edumaxsantos.drop.asteroid.components.SpriteComponent;
import com.github.edumaxsantos.drop.asteroid.components.VelocityComponent;

public class MovementSystem extends IteratingSystem {

    public MovementSystem() {
        super(Family.all(PositionComponent.class, VelocityComponent.class, SpriteComponent.class, BodyComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        var position = entity.getComponent(PositionComponent.class);
        var velocity = entity.getComponent(VelocityComponent.class);
        var sprite = entity.getComponent(SpriteComponent.class);
        var body = entity.getComponent(BodyComponent.class);

        position.setX(position.getX() + velocity.getX() * deltaTime);
        position.setY(position.getY() + velocity.getY() * deltaTime);

        sprite.getSprite().setPosition(position.getX() - sprite.getSprite().getWidth() / 2, position.getY() - sprite.getSprite().getHeight() / 2);

        body.body.setTransform(position.position, body.body.getAngle());
    }
}
