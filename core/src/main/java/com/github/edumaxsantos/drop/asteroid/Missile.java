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
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.time.temporal.ValueRange;

public class Missile extends Actor {

    public final static int SIZE = 25;
    private final static int SPEED = 700;
    private final Vector2 p1;
    private final Vector2 p2;
    private final ShapeDrawer drawer;
    private final Vector2 normalizedVector;
    private final Body body;
    private final float damage;

    public Missile(Ship ship, Batch batch, Vector2 shipPosition, Vector2 mouse, Body body) {
        this.setParent(ship);
        this.body = body;
        var pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        TextureRegion textureRegion = new TextureRegion(new Texture(pixmap));
        pixmap.dispose();

        p1 = shipPosition.cpy();
        p2 = p1.cpy();

        normalizedVector = mouse.cpy().sub(p1).nor();

        p2.mulAdd(normalizedVector, SIZE);

        drawer = new ShapeDrawer(batch, textureRegion);

        body.setUserData(this);
        damage = 50f;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.begin();
        drawer.line(p1, p2);
        batch.end();
    }

    @Override
    public void act(float delta) {
        var velocity = SPEED * delta;
        var translatedVector = new Vector2(normalizedVector.x * velocity, normalizedVector.y * velocity);

        p1.add(translatedVector);
        p2.add(translatedVector);

        body.setTransform(p2.x, p2.y, calculateAngle(p1, p2));


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

    public float getDamage() {
        return damage;
    }
}
