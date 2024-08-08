package com.github.edumaxsantos.drop.asteroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen extends ScreenAdapter {
    private final AsteroidGame asteroidGame;
    private final OrthographicCamera camera;

    public MainMenuScreen(AsteroidGame asteroidGame) {
        this.asteroidGame = asteroidGame;
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        asteroidGame.resetPoints();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        var batch = asteroidGame.stage.getBatch();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        asteroidGame.font.draw(batch, "Welcome to Asteroid!!!", 100, 150);
        asteroidGame.font.draw(batch, "Tap anywhere to begin!", 100, 100);
        batch.end();

        if (Gdx.input.isTouched()) {
            asteroidGame.setScreen(new GameScreen(asteroidGame));
            dispose();
        }
    }
}
