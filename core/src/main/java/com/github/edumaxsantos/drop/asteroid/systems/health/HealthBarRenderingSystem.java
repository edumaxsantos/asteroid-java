package com.github.edumaxsantos.drop.asteroid.systems.health;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.github.edumaxsantos.drop.asteroid.components.BodyComponent;
import com.github.edumaxsantos.drop.asteroid.components.PositionComponent;
import com.github.edumaxsantos.drop.asteroid.components.health.HealthBarComponent;
import com.github.edumaxsantos.drop.asteroid.components.health.HealthComponent;

public class HealthBarRenderingSystem extends IteratingSystem {

    private final static float MAX_SIZE = 100;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    public HealthBarRenderingSystem(SpriteBatch batch) {
        super(Family.all(PositionComponent.class, HealthComponent.class, HealthBarComponent.class).get());
        this.batch = batch;
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        var position = entity.getComponent(PositionComponent.class);
        var health = entity.getComponent(HealthComponent.class);
        var healthBar = entity.getComponent(HealthBarComponent.class);

        if (healthBar.timeVisible > 0) {
            var x = position.getX() - 30;
            var y = position.getY() + 75;

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            shapeRenderer.setColor(Color.GRAY);
            shapeRenderer.rect(x - 25, y, MAX_SIZE, 5);

            shapeRenderer.setColor(Color.GREEN);
            var currentWidth = MathUtils.map(0, health.maxHealth, 0, MAX_SIZE, health.health);
            shapeRenderer.rect(x - 25, y, currentWidth, 5);

            shapeRenderer.end();

            /*batch.begin();
            font.draw(batch, "HP: " + health.health, x - 25, y + 15);
            batch.end();*/
        }
    }

    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
    }
}
