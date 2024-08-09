package com.github.edumaxsantos.drop.asteroid;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.edumaxsantos.drop.asteroid.components.*;
import com.github.edumaxsantos.drop.asteroid.components.health.HealthBarComponent;
import com.github.edumaxsantos.drop.asteroid.components.health.HealthComponent;
import com.github.edumaxsantos.drop.asteroid.data.EntityData;
import com.github.edumaxsantos.drop.asteroid.enums.ElementType;
import com.github.edumaxsantos.drop.asteroid.listeners.CollisionListener;
import com.github.edumaxsantos.drop.asteroid.systems.*;
import com.github.edumaxsantos.drop.asteroid.systems.health.HealthBarRenderingSystem;
import com.github.edumaxsantos.drop.asteroid.systems.health.HealthSystem;

import static com.github.edumaxsantos.drop.asteroid.BodyFactory.createBox;
import static com.github.edumaxsantos.drop.asteroid.BodyFactory.createCircle;

public class GameScreen extends ScreenAdapter {

    private final AsteroidGame game;
    private final Engine engine;
    private SpriteBatch batch;
    private ScreenViewport viewport;
    private final Box2DDebugRenderer debugRenderer;
    public final World world;
    private final OrthographicCamera camera;
    private HealthBarRenderingSystem healthBarRenderingSystem;
    private GridRenderingSystem gridRenderingSystem;

    private final CollisionListener collisionListener;

    public GameScreen(AsteroidGame game) {
        this.game = game;
        Box2D.init();
        this.world = new World(new Vector2(0, 0), true);
        this.debugRenderer = new Box2DDebugRenderer();

        camera = new OrthographicCamera();
        engine = new Engine();
        collisionListener = new CollisionListener(engine, world);
    }

    @Override
    public void show() {
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);


        batch = new SpriteBatch();
        viewport = new ScreenViewport();
        viewport.setCamera(camera);

        world.setContactListener(collisionListener);

        healthBarRenderingSystem = new HealthBarRenderingSystem();
        gridRenderingSystem = new GridRenderingSystem(camera);

        var player = createPlayer();
        engine.addEntity(player);

        engine.addSystem(new InputSystem(player, world));
        engine.addSystem(new PlayerMovementSystem(350f));
        engine.addSystem(new RotationSystem());
        engine.addSystem(new RenderingSystem(batch));
        engine.addSystem(new MovementSystem());
        engine.addSystem(new HealthSystem(world));
        engine.addSystem(healthBarRenderingSystem);
        engine.addSystem(gridRenderingSystem);



        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                var asteroid = createAsteroid();
                engine.addEntity(asteroid);
            }
        }, 1, 1);
    }

    private Entity createAsteroid() {
        var asteroid = new Entity();

        var screenWidth = Gdx.graphics.getWidth();
        var screenHeight = Gdx.graphics.getHeight();

        int side = MathUtils.random(0, 3);

        var spawnSide = switch (side) {
            // left
            case 0 -> new Vector2(-50, MathUtils.random(0, screenHeight));
            case 1 -> new Vector2(screenWidth + 50, MathUtils.random(0, screenHeight));
            case 2 -> new Vector2(MathUtils.random(0, screenWidth), screenHeight + 50);
            case 3 -> new Vector2(MathUtils.random(0, screenHeight), -50);
            default -> new Vector2(0, 0);
        };

        var position = new PositionComponent();
        position.set(spawnSide);

        var sprite = new SpriteComponent(new Texture(Gdx.files.internal("asteroid/meteorBrown_small2.png")));
        sprite.getSprite().setPosition(position.getX() - sprite.getSprite().getWidth() / 2, position.getY() - sprite.getSprite().getHeight() / 2);

        var velocity = new VelocityComponent();
        var velocityX = MathUtils.random(-300, 300);
        var velocityY = MathUtils.random(-300, 300);

        if (side == 0) velocityX = Math.abs(velocityX);
        if (side == 1) velocityX = -Math.abs(velocityX);
        if (side == 2) velocityY = -Math.abs(velocityY);
        if (side == 3) velocityY = Math.abs(velocityY);

        velocity.set(velocityX, velocityY);

        var body = createCircle(world, position.position, sprite.getSprite().getWidth(), BodyDef.BodyType.DynamicBody);
        var bodyComponent = new BodyComponent(body);

        body.setUserData(new EntityData(ElementType.ASTEROID, asteroid));

        var damage = new DamageComponent(50);
        var health = new HealthComponent(100);
        var healthBar = new HealthBarComponent();

        asteroid.add(position);
        asteroid.add(sprite);
        asteroid.add(velocity);
        asteroid.add(bodyComponent);
        asteroid.add(damage);
        asteroid.add(health);
        asteroid.add(healthBar);

        return asteroid;
    }

    private Entity createPlayer() {
        var player = new Entity();

        var position = new PositionComponent();
        var initialX = (float) Gdx.graphics.getWidth() / 2;
        var initialY = (float) Gdx.graphics.getHeight() / 2;
        position.set(initialX, initialY);

        var sprite = new SpriteComponent(new Texture(Gdx.files.internal("asteroid/playerShip1_green.png")));

        var body = createBox(world, position.position, sprite.getSprite().getWidth(), sprite.getSprite().getHeight(), BodyDef.BodyType.DynamicBody);
        var bodyComponent = new BodyComponent(body);

        var input = new InputComponent();
        var rotation = new RotationComponent();
        var health = new HealthComponent(500);
        var healthBar = new HealthBarComponent();
        var damage = new DamageComponent(75);


        body.setUserData(new EntityData(ElementType.PLAYER, player));

        player.add(position);
        player.add(sprite);
        player.add(bodyComponent);
        player.add(input);
        player.add(rotation);
        player.add(health);
        player.add(healthBar);
        player.add(damage);

        return player;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        world.step(1 / 60f, 6, 2);
        viewport.apply();

        collisionListener.processPendingDestruction();

        debugRenderer.render(world, game.stage.getCamera().combined);

        engine.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        world.dispose();
        healthBarRenderingSystem.dispose();
        gridRenderingSystem.dispose();
        batch.dispose();
        debugRenderer.dispose();
        game.dispose();
    }
}
