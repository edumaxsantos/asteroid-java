package com.github.edumaxsantos.drop.asteroid.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.edumaxsantos.drop.asteroid.components.*;
import com.github.edumaxsantos.drop.asteroid.data.EntityData;
import com.github.edumaxsantos.drop.asteroid.enums.ElementType;

import static com.github.edumaxsantos.drop.asteroid.BodyFactory.createBox;

public class InputSystem extends IteratingSystem {
    private static final long SHOOT_COOLDOWN = 500;
    private long lastShotTime;

    private final Entity player;
    private final World world;

    public InputSystem(Entity player, World world) {
        super(Family.all(InputComponent.class, PositionComponent.class, RotationComponent.class).get());
        this.player = player;
        this.world = world;
        this.lastShotTime = 0;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            long currentTime = TimeUtils.millis();
            var mouseX = Gdx.input.getX();
            var mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (currentTime - lastShotTime >= SHOOT_COOLDOWN) {
                var mousePos = new Vector2(mouseX, mouseY);
                shootMissile(mousePos);
                lastShotTime = currentTime;
            }

        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        var input = entity.getComponent(InputComponent.class);
        var position = entity.getComponent(PositionComponent.class);
        var rotation = entity.getComponent(RotationComponent.class);

        captureInput(input);


        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY(); // do not forget this.

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

    private void shootMissile(Vector2 mousePos) {
        var missile = createMissile(mousePos);
        getEngine().addEntity(missile);
    }

    private Entity createMissile(Vector2 mousePos) {
        var missile = new Entity();

        var position = new PositionComponent();
        var velocity = new VelocityComponent();
        var missileSpeed = 600;

        var playerPosition = player.getComponent(PositionComponent.class);
        var playerRotation = player.getComponent(RotationComponent.class);
        var playerDamage = player.getComponent(DamageComponent.class);

        var angle = playerRotation.angle + 90;
        var offsetX = MathUtils.cosDeg(angle) * 30;
        var offsetY = MathUtils.sinDeg(angle) * 30;

        position.set(playerPosition.getX() + offsetX, playerPosition.getY() + offsetY);

        var direction = new Vector2(mousePos.x - playerPosition.getX(), mousePos.y - playerPosition.getY()).nor();

        velocity.set(direction.x * missileSpeed, direction.y * missileSpeed);

        var sprite = new SpriteComponent(new Texture(Gdx.files.internal("asteroid/laserBlue03.png")));

        sprite.getSprite().setPosition(position.getX(), position.getY());

        var angleToTarget = MathUtils.atan2(direction.y, direction.x) * MathUtils.radiansToDegrees;
        angleToTarget += 90;
        sprite.getSprite().setRotation(angleToTarget);

        var body = new BodyComponent(createBox(world, new Vector2(position.getX(), position.getY()), sprite.getSprite().getWidth(), sprite.getSprite().getHeight(), BodyDef.BodyType.DynamicBody));
        body.body.setUserData(new EntityData(ElementType.MISSILE, missile));

        body.body.setTransform(body.body.getPosition(), angleToTarget * MathUtils.degreesToRadians);


        var damage = new DamageComponent(playerDamage.damage);

        missile.add(position);
        missile.add(sprite);
        missile.add(body);
        missile.add(velocity);
        missile.add(damage);

        return missile;
    }
}
