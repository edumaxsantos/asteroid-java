package com.github.edumaxsantos.drop.asteroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

import java.time.temporal.ValueRange;

public class Asteroid extends Actor {
    private final static Texture texture = new Texture(Gdx.files.internal("asteroid/meteorBrown_small2.png"));
    private float x;
    private float y;
    private final boolean goDown;
    private final boolean goLeft;
    private final float velocity;
    private final Body body;
    private boolean destroyed;
    private final HealthComponent healthComponent;
    private final HealthBar healthBar;
    private final float damage;
    Asteroid(float x, float y, boolean goDown, boolean goLeft, float velocity, Body body) {
        this.x = x;
        this.y = y;
        this.goDown = goDown;
        this.goLeft = goLeft;
        this.velocity = velocity;
        this.body = body;
        this.setSize(texture.getWidth(), texture.getHeight());

        this.setOrigin(Align.center);

        this.body.setUserData(this);

        destroyed = false;

        this.healthComponent = new HealthComponent(100);
        this.damage = 75;

        this.healthBar = new HealthBar(this, healthComponent, false);

    }

    public static Asteroid random(Body body) {
        var leftOfScreen = MathUtils.random(-50, 0);
        var rightOfScreen = MathUtils.random(Gdx.graphics.getWidth(), Gdx.graphics.getWidth() + 50);

        var x = MathUtils.randomSign() > 0 ? leftOfScreen : rightOfScreen;

        var topOfScreen = MathUtils.random(-50, 0);
        var bottomOfScreen = MathUtils.random(Gdx.graphics.getHeight(), Gdx.graphics.getHeight() + 50);

        var y = MathUtils.randomSign() > 0 ? topOfScreen : bottomOfScreen;

        var velocity = MathUtils.random(50, 350);
        var centerX = Gdx.graphics.getWidth() / 2;
        var centerY = Gdx.graphics.getHeight() / 2;
        boolean goDown = y > centerY;
        boolean goLeft = x > centerX;

        return new Asteroid(x, y, goDown, goLeft, velocity, body);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, x, y);
        healthBar.draw(batch, parentAlpha);
        //body.setTransform(getX() + Asteroid.getRealWidth() / 2, getY() + Asteroid.getRealHeight() / 2, 0);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        updateAsteroid();
        healthBar.act(delta);
    }

    public boolean isVisible() {
        return x > 0 && x < Gdx.graphics.getWidth() && y > 0 && y < Gdx.graphics.getHeight();
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

        body.setTransform(getX() + (float) Asteroid.getRealWidth() / 2, getY() + (float) Asteroid.getRealHeight() / 2, body.getAngle());

        handleBoundaries();
    }

    private void handleBoundaries() {
        var widthBounds = ValueRange.of(0, Gdx.graphics.getWidth());
        var heightBounds = ValueRange.of(0, Gdx.graphics.getHeight());

        if (widthBounds.isValidIntValue( (long) body.getPosition().x) && heightBounds.isValidIntValue( (long) body.getPosition().y)) {
            setVisible(true);
        }
    }

    public Body getBody() {
        return body;
    }

    public boolean shouldBeDestroyed() {
        return destroyed;
    }

    public void destroy() {
        destroyed = true;
    }



    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    public static int getRealWidth() {
        return texture.getWidth();
    }

    public static int getRealHeight() {
        return texture.getHeight();
    }

    public float getDamage() {
        return damage;
    }

    public void takeDamage(float damage) {
        healthComponent.takeDamage(damage);
        healthBar.setVisible(true);
    }

    public boolean isDead() {
        return healthComponent.isDead();
    }
}
