package com.github.edumaxsantos.drop.asteroid.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.github.edumaxsantos.drop.asteroid.components.ScoreComponent;

public class ScoreRenderingSystem extends IteratingSystem {
    private final Batch batch;
    private final BitmapFont font;
    public ScoreRenderingSystem(Batch batch) {
        super(Family.all(ScoreComponent.class).get());
        this.batch = batch;
        this.font = new BitmapFont();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        var scoreComponent = entity.getComponent(ScoreComponent.class);

        batch.begin();
        font.setColor(Color.WHITE);
        font.draw(batch, "Points: " + scoreComponent.score, 10, Gdx.graphics.getHeight() - 10);
        batch.end();
    }
}
