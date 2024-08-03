package com.github.edumaxsantos.drop.asteroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.concurrent.TimeUnit;

import static com.github.edumaxsantos.drop.asteroid.BodyFactory.createBox;
import static com.github.edumaxsantos.drop.asteroid.BodyFactory.createCircle;

public class AsteroidGameScreen extends ScreenAdapter {

    public final AsteroidGame game;
    private final Array<FlyingObject> flyingObjects;
    private final Ship ship;
    private final Stage stage;
    private final Box2DDebugRenderer debugRenderer;
    public final World world;

    public AsteroidGameScreen(AsteroidGame asteroidGame) {
        this.game = asteroidGame;

        Box2D.init();
        this.stage = new Stage(new ScreenViewport());

        this.flyingObjects = new Array<>();
        this.world = new World(new Vector2(0, 0), true);

        var shipX = Ship.getRealWidth();
        var shipY = Ship.getRealHeight();

        var shipBody = createBox(world, (float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2, shipX, shipY, BodyDef.BodyType.DynamicBody);

        ship = new Ship(this, shipBody);

        stage.addActor(ship);

        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);

        this.debugRenderer = new Box2DDebugRenderer();


        world.setContactListener(new MyContactListener());

        handleAsteroids();
        handleStarGolds();

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        stage.getCamera().update();

        world.step(1 /60f, 6, 2);
        debugRenderer.render(world, stage.getCamera().combined);

        stage.getBatch().setProjectionMatrix(stage.getCamera().combined);
        stage.getBatch().begin();
        ship.act(Gdx.graphics.getDeltaTime());
        ship.draw(stage.getBatch(), 1);
        this.game.font.draw(stage.getBatch(), "Hits: " + game.getPoints(), 5, Gdx.graphics.getHeight() - 5);
        this.game.font.draw(stage.getBatch(), "Health: " + ship.getHealthComponent().getCurrentHealth(), Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 5);


        for (var flyingObject : flyingObjects) {
            flyingObject.act(delta);
            flyingObject.draw(stage.getBatch(), 1);
        }

        stage.getBatch().end();

        for (var asteroid : flyingObjects) {
            if (asteroid.shouldBeDestroyed()) {
                world.destroyBody(asteroid.getBody());
                flyingObjects.removeValue(asteroid, false);
            }
        }
    }

    @Override
    public void dispose() {
        world.dispose();
        stage.dispose();
        flyingObjects.clear();
        debugRenderer.dispose();
        game.dispose();
    }

    private void handleStarGolds() {

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                spawnStarGold();
            }
        }, TimeUnit.SECONDS.toSeconds(10), TimeUnit.SECONDS.toSeconds(10));
    }

    private void handleAsteroids() {

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                spawnAsteroid();
            }
        }, TimeUnit.SECONDS.toSeconds(1), TimeUnit.SECONDS.toSeconds(1));
    }

    private void spawnStarGold() {
        var starGoldBody = createCircle(world, 50, 50, StarGold.getRealWidth(), BodyDef.BodyType.DynamicBody);
        var starGold = StarGold.random(starGoldBody);

        flyingObjects.add(starGold);
    }

    private void spawnAsteroid() {
        var asteroidBody = createCircle(world, 50, 50, Asteroid.getRealWidth(), BodyDef.BodyType.DynamicBody);
        var asteroid = Asteroid.random(asteroidBody);

        flyingObjects.add(asteroid);
    }
}
