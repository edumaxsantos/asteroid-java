package com.github.edumaxsantos.drop.asteroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.concurrent.TimeUnit;

import static com.github.edumaxsantos.drop.asteroid.BodyFactory.createBox;

public class Ship extends Group {
    private final static Texture texture = new Texture(Gdx.files.internal("asteroid/playerShip1_green.png"));
    private final static float scale = 0.5f;
    public final AsteroidGameScreen screen;
    private static final float SPEED = 200;
    private static final long FIRING_SPEED_IN_MS = 500;
    private final Array<Missile> missiles;
    private float lastNano;
    private final Body body;
    private final HealthComponent healthComponent;
    private final HealthBar healthBar;

    public Ship(AsteroidGameScreen screen, Body body) {
        this.screen = screen;
        //sprite = new Sprite(texture);
        this.setSize(texture.getWidth(), texture.getHeight());
        this.setOrigin(Align.center);
        centerShip();
        this.setZIndex(90);
        this.setScale(scale);

        missiles = new Array<>();

        this.body = body;
        this.body.setUserData(this);
        healthComponent = new HealthComponent(500);

        healthBar = new HealthBar(this, healthComponent, true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        healthBar.draw(batch, parentAlpha);
        batch.draw(new TextureRegion(texture), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    @Override
    public void act(float delta) {
        var mouseX = Gdx.input.getX();
        var mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        var angle = calculateAngle(mouseX, mouseY);
        this.setRotation(angle);
        body.setTransform(getX() + Ship.getRealWidth(), getY() + Ship.getRealHeight(), angle);

        if (Gdx.input.isTouched()) {
            if (TimeUtils.nanoTime() - lastNano > TimeUnit.MILLISECONDS.toNanos(FIRING_SPEED_IN_MS)) {
                var initialPosition = new Vector2(getX() + getRealWidth(), getY() + getRealHeight());
                var mousePosition = new Vector2(mouseX, mouseY);
                var body = createBox(screen.world, initialPosition.x, initialPosition.y, 1, Missile.SIZE, BodyDef.BodyType.DynamicBody);
                missiles.add(new Missile(this, screen.game.stage.getBatch(), initialPosition, mousePosition, body));
                lastNano = TimeUtils.nanoTime();
            }

        }

        handleMovement(delta);
        for(var missile : missiles) {
            if (!missile.isVisible()) {
                missiles.removeValue(missile, false);
                screen.world.destroyBody(missile.getBody());
                continue;
            }
            missile.act(delta);
            missile.draw(screen.game.stage.getBatch(), 1);
        }

        healthBar.act(delta);
    }

    public boolean isDead() {
        return healthComponent.isDead();
    }

    public void heal(float amount) {
        healthComponent.heal(amount);
        healthBar.setVisible(true);
    }

    public void takeDamage(float damage) {
        healthComponent.takeDamage(damage);
        healthBar.setVisible(true);
    }

    private void centerShip() {
        this.setPosition((float) Gdx.graphics.getWidth() / 2 - this.getWidth() /2, (float) Gdx.graphics.getHeight() / 2 - this.getHeight() / 2);
    }

    private float calculateAngle(float mouseX, float mouseY) {

        var x = this.getX() + this.getOriginX();
        var y = this.getY() + this.getOriginY();

        return (MathUtils.atan2(mouseY - y, mouseX - x) * MathUtils.radiansToDegrees) - 90;
    }

    private void handleMovement(float delta) {
        var velocity = SPEED * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.setX(this.getX() - velocity);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.setX(this.getX() + velocity);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            this.setY(this.getY() + velocity);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            this.setY(this.getY() - velocity);
        }
    }

    public static float getRealHeight() {
        return texture.getHeight() * scale;
    }

    public static float getRealWidth() {
        return texture.getWidth() * scale;
    }

    public HealthComponent getHealthComponent() {
        return healthComponent;
    }

    public void destroy() {
        screen.game.setScreen(new EndGameScreen(screen.game));
    }
}
