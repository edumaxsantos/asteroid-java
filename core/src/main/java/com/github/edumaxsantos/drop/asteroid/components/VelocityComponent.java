package com.github.edumaxsantos.drop.asteroid.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class VelocityComponent implements Component {
    public Vector2 velocity = new Vector2();

    public void set(float x, float y) {
        velocity.set(x, y);
    }

    public void setX(float x) {
        velocity.x = x;
    }

    public void setY(float y) {
        velocity.y = y;
    }

    public float getX() {
        return velocity.x;
    }

    public float getY() {
        return velocity.y;
    }
}
