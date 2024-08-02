package com.github.edumaxsantos.drop.asteroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.concurrent.TimeUnit;

public class AsteroidGameScreen extends ScreenAdapter {

    public final AsteroidGame game;
    private final Array<Asteroid> asteroids;
    long lastAsteroidTime;
    private final Ship ship;
    private final Stage stage;
    private final Box2DDebugRenderer debugRenderer;
    public final World world;

    public AsteroidGameScreen(AsteroidGame asteroidGame) {
        this.game = asteroidGame;

        Box2D.init();
        this.stage = new Stage(new ScreenViewport());

        this.asteroids = new Array<>();
        this.world = new World(new Vector2(0, 0), true);

        var shipX = Ship.getRealWidth();
        var shipY = Ship.getRealHeight();

        var shipBody = createBox((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2, shipX, shipY, BodyDef.BodyType.DynamicBody);

        ship = new Ship(this, shipBody);

        stage.addActor(ship);

        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);

        this.debugRenderer = new Box2DDebugRenderer();


        world.setContactListener(new MyContactListener());

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        stage.getCamera().update();

        world.step(1 /60f, 6, 2);
        //debugRenderer.render(world, stage.getCamera().combined);

        stage.getBatch().setProjectionMatrix(stage.getCamera().combined);
        stage.getBatch().begin();
        ship.act(Gdx.graphics.getDeltaTime());
        ship.draw(stage.getBatch(), 1);
        this.game.font.draw(stage.getBatch(), "Hits: " + game.getPoints(), 5, Gdx.graphics.getHeight() - 5);
        this.game.font.draw(stage.getBatch(), "Health: " + ship.getHealthComponent().getCurrentHealth(), Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 5);
        handleAsteroids();

        stage.getBatch().end();

        for (var asteroid : asteroids) {
            if (asteroid.shouldBeDestroyed()) {
                world.destroyBody(asteroid.getBody());
                asteroids.removeValue(asteroid, false);
            }
        }
    }

    @Override
    public void dispose() {
        world.dispose();
        stage.dispose();
        asteroids.clear();
        debugRenderer.dispose();
        game.dispose();
    }

    public Body createBox(float x, float y, float width, float height, BodyDef.BodyType bodyType) {
        var bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(x, y);

        var body = world.createBody(bodyDef);

        var shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        body.createFixture(shape, 1f);
        shape.dispose();

        return body;
    }

    private void handleAsteroids() {
        if (TimeUtils.nanoTime() - lastAsteroidTime > TimeUnit.SECONDS.toNanos(1)) {
            spawnAsteroid();
        }

        for (var asteroid : asteroids) {
            asteroid.act(TimeUtils.nanoTime());
            asteroid.draw(stage.getBatch(), 1);
        }
    }

    private void spawnAsteroid() {
        var asteroidBody = createBox(50, 50, Asteroid.getRealWidth(), Asteroid.getRealHeight(), BodyDef.BodyType.DynamicBody);
        var asteroid = Asteroid.random(asteroidBody);

        asteroids.add(asteroid);
        lastAsteroidTime = TimeUtils.nanoTime();
    }
}
