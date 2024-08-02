package com.github.edumaxsantos.drop.asteroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class EndGameScreen extends ScreenAdapter {
    private final AsteroidGame asteroidGame;
    private final OrthographicCamera camera;

    public EndGameScreen(AsteroidGame asteroidGame) {
        this.asteroidGame = asteroidGame;
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        var batch = asteroidGame.stage.getBatch();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        asteroidGame.font.draw(batch, "Your ship got destroyed.", 100, 150);
        asteroidGame.font.draw(batch, "You destroyed: " + asteroidGame.getPoints() + " asteroids.", 100, 125);
        asteroidGame.font.draw(batch, "Tap anywhere to restart!", 100, 100);
        batch.end();

        if (Gdx.input.isTouched()) {
            asteroidGame.setScreen(new AsteroidGameScreen(asteroidGame));
            dispose();
        }
    }
}
