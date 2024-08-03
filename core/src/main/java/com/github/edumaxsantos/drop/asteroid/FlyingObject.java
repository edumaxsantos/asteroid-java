package com.github.edumaxsantos.drop.asteroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

import java.time.temporal.ValueRange;

public class FlyingObject extends Actor {
    protected final Texture texture;
    protected float x;
    protected float y;
    protected final boolean goDown;
    protected final boolean goLeft;
    protected final float velocity;
    protected final Body body;
    protected boolean destroyed;
    protected final HealthComponent healthComponent;
    protected final HealthBar healthBar;
    protected final float damage;

    public FlyingObject(Texture texture, float x, float y, boolean goDown, boolean goLeft, float velocity, Body body) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.goDown = goDown;
        this.goLeft = goLeft;
        this.velocity = velocity;
        this.body = body;
        this.setSize(texture.getWidth(), texture.getHeight());

        this.setPosition(x, y);

        this.setOrigin(Align.center);

        this.body.setUserData(this);

        destroyed = false;

        this.healthComponent = new HealthComponent(100);
        this.damage = 75;

        this.healthBar = new HealthBar(this, healthComponent, false);
    }

    protected void handleBoundaries() {
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

    public static int getRealWidth() {
        throw new RuntimeException("getRealWidth method should be implemented on a subclass");
    }

    public static int getRealHeight() {
        throw new RuntimeException("getRealHeight method should be implemented on a subclass");
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

    @Override
    public boolean isVisible() {
        return x > 0 && x < Gdx.graphics.getWidth() && y > 0 && y < Gdx.graphics.getHeight();
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, x, y);
        healthBar.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        healthBar.act(delta);
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }
}
