package com.github.edumaxsantos.drop.asteroid;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;


public class AsteroidGame extends Game {
    public BitmapFont font;
    public Stage stage;
    private int points;

    @Override
    public void create() {
        Box2D.init();
        font = new BitmapFont();
        stage = new Stage();
        this.setScreen(new MainMenuScreen(this));
        points = 0;
    }

    @Override
    public void dispose() {
        font.dispose();
        stage.dispose();
    }

    public int getPoints() {
        return points;
    }

    public void increasePoint() {
        points++;
    }

    public void resetPoints() {
        points = 0;
    }
}
