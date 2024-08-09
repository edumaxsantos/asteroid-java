package com.github.edumaxsantos.drop.asteroid.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class PositionComponent implements Component {
    public Vector2 position = new Vector2();

    public void set(Vector2 position) {
        this.position.set(position.cpy());
    }

    public void set(float x, float y) {
        position.set(x, y);
    }

    public void setX(float x) {
        position.x = x;
    }

    public void setY(float y) {
        position.y = y;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    @Override
    public String toString() {
        return "PositionComponent [x=" + position.x + ", y="+ position.y + "]";
    }
}
