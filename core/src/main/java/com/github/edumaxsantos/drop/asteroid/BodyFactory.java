package com.github.edumaxsantos.drop.asteroid;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class BodyFactory {

    public static Body createCircle(World world, Vector2 position, float width, BodyDef.BodyType bodyType) {
        var bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(position);

        var body = world.createBody(bodyDef);

        var shape = new CircleShape();
        shape.setRadius(width / 2);

        body.createFixture(shape, 1f);
        shape.dispose();

        return body;
    }

    public static Body createBox(World world, Vector2 position, float width, float height, BodyDef.BodyType bodyType) {
        var bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(position);

        var body = world.createBody(bodyDef);

        var shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        body.createFixture(shape, 1f);
        shape.dispose();

        return body;
    }
}
