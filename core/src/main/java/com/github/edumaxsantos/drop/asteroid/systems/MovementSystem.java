package com.github.edumaxsantos.drop.asteroid.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import com.github.edumaxsantos.drop.asteroid.components.BodyComponent;
import com.github.edumaxsantos.drop.asteroid.components.PositionComponent;
import com.github.edumaxsantos.drop.asteroid.components.SpriteComponent;
import com.github.edumaxsantos.drop.asteroid.components.VelocityComponent;
import com.github.edumaxsantos.drop.asteroid.data.EntityData;
import com.github.edumaxsantos.drop.asteroid.enums.ElementType;

public class MovementSystem extends IteratingSystem {
    private final World world;
    public MovementSystem(World world) {
        super(Family.all(PositionComponent.class, VelocityComponent.class, SpriteComponent.class, BodyComponent.class).get());
        this.world = world;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        var position = entity.getComponent(PositionComponent.class);
        var velocity = entity.getComponent(VelocityComponent.class);
        var sprite = entity.getComponent(SpriteComponent.class);
        var body = entity.getComponent(BodyComponent.class);

        if (isOutOfBounds(position.getX(), position.getY())) {
            getEngine().removeEntity(entity);
            world.destroyBody(body.body);
            return;
        }

        var entityData = (EntityData) body.body.getUserData();
        if (entityData.is(ElementType.MISSILE)) {
            System.out.println("here");
        }

        position.setX(position.getX() + velocity.getX() * deltaTime);
        position.setY(position.getY() + velocity.getY() * deltaTime);

        sprite.getSprite().setPosition(position.getX() - sprite.getSprite().getWidth() / 2, position.getY() - sprite.getSprite().getHeight() / 2);

        body.body.setTransform(position.position, body.body.getAngle());
    }

    private boolean isOutOfBounds(float x, float y) {
        final int MARGIN = 60;
        return x < -MARGIN || x > Gdx.graphics.getWidth() + MARGIN || y < -MARGIN || y > Gdx.graphics.getHeight() + MARGIN;
    }
}
