package com.github.edumaxsantos.drop.asteroid;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.TimeUtils;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.concurrent.TimeUnit;

public class HealthBar extends Actor {
    private final HealthComponent healthComponent;
    private long createdTime;
    private ShapeDrawer shapeDrawer;
    private final Rectangle rectangle;
    private final Actor actor;
    private final TextureRegion textureRegion;

    public HealthBar(Actor actor, HealthComponent healthComponent, boolean startVisible) {
        this.actor = actor;
        this.healthComponent = healthComponent;
        createdTime = TimeUtils.nanoTime();
        setVisible(startVisible);


        rectangle = new Rectangle();
        rectangle.setSize(100, 5);
        var pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        textureRegion = new TextureRegion(new Texture(pixmap));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!isVisible()) {
            return;
        }

        if (shapeDrawer == null) {
            shapeDrawer = new ShapeDrawer(batch, textureRegion);
        }

        float maxHealth = healthComponent.getMaxHealth();
        float currentHealth = healthComponent.getCurrentHealth();
        float healthPercentage = MathUtils.map(0, maxHealth, 0, 100, currentHealth);


        shapeDrawer.setColor(Color.GRAY);
        rectangle.setSize(100, 5);
        rectangle.setPosition(actor.getX(), actor.getY() + actor.getHeight());
        shapeDrawer.filledRectangle(rectangle);

        shapeDrawer.setColor(Color.GREEN);
        rectangle.setSize(healthPercentage, 5);
        rectangle.setPosition(actor.getX(), actor.getY() + actor.getHeight());
        shapeDrawer.filledRectangle(rectangle);
    }

    @Override
    public void act(float delta) {
        if (isVisible() && TimeUtils.nanoTime() - createdTime > TimeUnit.SECONDS.toNanos(1)) {
            setVisible(false);
        }
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        createdTime = TimeUtils.nanoTime();
    }
}
