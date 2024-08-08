package com.github.edumaxsantos.drop.asteroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.time.temporal.ValueRange;
import java.util.concurrent.TimeUnit;

public class Missile extends Actor {
    private final static Texture texture = new Texture(Gdx.files.internal("asteroid/laserBlue03.png"));

    public final static int SIZE = 25;
    private final static int SPEED = 700;
    private final Vector2 p1;
    private final Vector2 p2;
    private final Vector2 normalizedVector;
    private final Body body;
    private final float damage;
    private float initial;

    public Missile(Ship ship, Batch batch, Vector2 shipPosition, Vector2 mouse, Body body) {
        this.setParent(ship);
        this.body = body;

        p1 = shipPosition.cpy();
        p2 = p1.cpy();

        normalizedVector = mouse.cpy().sub(p1).nor();

        p2.mulAdd(normalizedVector, SIZE);

        body.setUserData(this);
        damage = 50f;

        System.out.println(body.getPosition());
        System.out.println(getPosition());

        //setPosition(body.getPosition().x, body.getPosition().y);

        setWidth(texture.getWidth());
        setHeight(texture.getHeight());
        setOrigin(getX(), getY());
        setScale(1f);
        initial = Gdx.graphics.getDeltaTime();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.begin();
        setRotation(body.getAngle() * MathUtils.radiansToDegrees);
        //System.out.println(getRotation());
        batch.draw(new TextureRegion(texture), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        //drawer.line(p1, p2);
        batch.end();
    }

    @Override
    public void act(float delta) {
        var velocity = SPEED * delta;
        var translatedVector = new Vector2(normalizedVector.x * velocity, normalizedVector.y * velocity);

        p1.add(translatedVector);
        p2.add(translatedVector);

        if (delta - initial > 0) {
            System.out.println("here");
        }

        //body.setTransform(getX(), getY(), translatedVector.angleDeg());

        body.setTransform(p1.x, p1.y, calculateAngle(p1, p2));

        setPosition(body.getPosition().x + getWidth(), body.getPosition().y);


        var widthBounds = ValueRange.of(0, Gdx.graphics.getWidth());
        var heightBounds = ValueRange.of(0, Gdx.graphics.getHeight());

        if (!widthBounds.isValidValue((long) p1.x) || !heightBounds.isValidValue((long) p1.y)) {
            setVisible(false);
        }
    }

    public void destroy() {
        setVisible(false);
    }

    private float calculateAngle(Vector2 p1, Vector2 p2) {
        return MathUtils.atan2(p2.y - p1.y, p2.x - p1.x) + (90 * MathUtils.degreesToRadians);
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return new Vector2(getX(), getY());
    }

    public float getDamage() {
        return damage;
    }

    public static int getRealWidth() {
        return texture.getWidth();
    }

    public static int getRealHeight() {
        return texture.getHeight();
    }
}
