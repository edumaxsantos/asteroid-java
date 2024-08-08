package com.github.edumaxsantos.drop.asteroid.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.github.edumaxsantos.drop.asteroid.components.InputComponent;
import com.github.edumaxsantos.drop.asteroid.components.PositionComponent;
import com.github.edumaxsantos.drop.asteroid.components.RotationComponent;
import com.github.edumaxsantos.drop.asteroid.components.SpriteComponent;

public class InputSystem extends IteratingSystem {
    private OrthographicCamera camera;

    public InputSystem(OrthographicCamera camera) {
        super(Family.all(InputComponent.class, PositionComponent.class, RotationComponent.class, SpriteComponent.class).get());
        this.camera = camera;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        var input = entity.getComponent(InputComponent.class);
        var position = entity.getComponent(PositionComponent.class);
        var rotation = entity.getComponent(RotationComponent.class);
        var sprite = entity.getComponent(SpriteComponent.class);

        captureInput(input);

        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY(); // do not forget this.

        var spritePosition = new Vector2(sprite.getSprite().getX(), sprite.getSprite().getY());

        var x = position.getX();
        var y = position.getY();

        var angle = (MathUtils.atan2(mouseY - y, mouseX - x) * MathUtils.radiansToDegrees);
        rotation.angle = angle - 90;
    }

    private void captureInput(InputComponent input) {
        input.left = Gdx.input.isKeyPressed(Input.Keys.A);
        input.right = Gdx.input.isKeyPressed(Input.Keys.D);
        input.up = Gdx.input.isKeyPressed(Input.Keys.W);
        input.down = Gdx.input.isKeyPressed(Input.Keys.S);
    }
}
