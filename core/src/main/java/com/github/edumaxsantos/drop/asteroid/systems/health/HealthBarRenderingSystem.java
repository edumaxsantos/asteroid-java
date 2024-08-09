package com.github.edumaxsantos.drop.asteroid.systems.health;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.github.edumaxsantos.drop.asteroid.components.PositionComponent;
import com.github.edumaxsantos.drop.asteroid.components.SpriteComponent;
import com.github.edumaxsantos.drop.asteroid.components.health.HealthBarComponent;
import com.github.edumaxsantos.drop.asteroid.components.health.HealthComponent;

public class HealthBarRenderingSystem extends IteratingSystem {

    private final static float MAX_SIZE = 100;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;

    public HealthBarRenderingSystem() {
        super(Family.all(PositionComponent.class, HealthComponent.class, HealthBarComponent.class, SpriteComponent.class).get());
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        var position = entity.getComponent(PositionComponent.class);
        var health = entity.getComponent(HealthComponent.class);
        var healthBar = entity.getComponent(HealthBarComponent.class);
        var sprite = entity.getComponent(SpriteComponent.class);

        if (healthBar.timeVisible > 0) {
            healthBar.timeVisible -= deltaTime;

            var x = position.getX() - 30;
            var y = position.getY() + sprite.getSprite().getHeight();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            shapeRenderer.setColor(Color.GRAY);
            shapeRenderer.rect(x - 25, y, MAX_SIZE, 5);

            shapeRenderer.setColor(Color.GREEN);
            var currentWidth = MathUtils.map(0, health.maxHealth, 0, MAX_SIZE, health.health);
            shapeRenderer.rect(x - 25, y, currentWidth, 5);

            shapeRenderer.end();
        }
    }

    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
    }
}
