package com.github.edumaxsantos.drop.asteroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;

public class Asteroid extends FlyingObject {
    private final static Texture texture = new Texture(Gdx.files.internal("asteroid/meteorBrown_small2.png"));

    private Asteroid(float x, float y, boolean goDown, boolean goLeft, float velocity, Body body) {
        super(texture, x, y, goDown, goLeft, velocity, body);
    }

    public static Asteroid random(Body body) {
        var x = body.getPosition().x;
        var y = body.getPosition().y;

        var velocity = MathUtils.random(50, 350);
        var centerX = Gdx.graphics.getWidth() / 2;
        var centerY = Gdx.graphics.getHeight() / 2;
        boolean goDown = y > centerY;
        boolean goLeft = x > centerX;

        return new Asteroid(x, y, goDown, goLeft, velocity, body);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        updateAsteroid();
    }

    private void updateAsteroid() {
        if (goDown) {
            y -= velocity * Gdx.graphics.getDeltaTime();
        } else {
            y += velocity * Gdx.graphics.getDeltaTime();
        }

        if (goLeft) {
            x -= velocity * Gdx.graphics.getDeltaTime();
        } else {
            x += velocity * Gdx.graphics.getDeltaTime();
        }

        body.setTransform(getX() + (float) getRealWidth() / 2, getY() + (float) getRealHeight() / 2, body.getAngle());

        handleBoundaries();
    }

    public static int getRealWidth() {
        return texture.getWidth();
    }

    public static int getRealHeight() {
        return texture.getHeight();
    }


}
