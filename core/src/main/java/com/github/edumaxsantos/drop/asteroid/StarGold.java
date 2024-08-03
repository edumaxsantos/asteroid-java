package com.github.edumaxsantos.drop.asteroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;

public class StarGold extends FlyingObject {
    private final static Texture texture = new Texture(Gdx.files.internal("asteroid/star_gold.png"));
    private StarGold(float x, float y, boolean goDown, boolean goLeft, float velocity, Body body) {
        super(texture, x, y, goDown, goLeft, velocity, body);
    }

    public static StarGold random(Body body) {
        int x, y;

        do {
            x = MathUtils.random(-50, Gdx.graphics.getWidth());
            y = MathUtils.random(-50, Gdx.graphics.getHeight());
        } while ((x > 0 && x < Gdx.graphics.getWidth()) && (y > 0 && y < Gdx.graphics.getHeight()));

        var velocity = 75;
        var centerX = Gdx.graphics.getWidth() / 2;
        var centerY = Gdx.graphics.getHeight() / 2;
        boolean goDown = y > centerY;
        boolean goLeft = x > centerX;

        return new StarGold(x, y, goDown, goLeft, velocity, body);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        updateStarGold();
    }

    private void updateStarGold() {
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
