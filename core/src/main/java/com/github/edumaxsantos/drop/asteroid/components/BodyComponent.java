package com.github.edumaxsantos.drop.asteroid.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

public class BodyComponent implements Component {
    public final Body body;

    public BodyComponent(final Body body) {
        this.body = body;
    }
}
