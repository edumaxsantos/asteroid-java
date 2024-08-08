package com.github.edumaxsantos.drop.asteroid.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.github.edumaxsantos.drop.asteroid.components.BodyComponent;
import com.github.edumaxsantos.drop.asteroid.components.InputComponent;
import com.github.edumaxsantos.drop.asteroid.components.PositionComponent;

public class PlayerMovementSystem extends IteratingSystem {
    private final float speed;

    public PlayerMovementSystem(float speed) {
        super(Family.all(InputComponent.class, PositionComponent.class).get());
        this.speed = speed;
    }

    public PlayerMovementSystem() {
        super(Family.all(InputComponent.class, BodyComponent.class).get());
        this.speed = 200f;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        var input = entity.getComponent(InputComponent.class);
        var position = entity.getComponent(PositionComponent.class);

        var movement = new Vector2();

        if (input.left) {
            movement.x -= 1;
        }
        if (input.right) {
            movement.x += 1;
        }

        if (input.up) {
            movement.y += 1;
        }
        if (input.down) {
            movement.y -= 1;
        }

        if (!movement.isZero()) {
            movement.nor();
            movement.scl(speed * deltaTime);
        }

        position.setX(position.getX() + movement.x);
        position.setY(position.getY() + movement.y);
    }
}
