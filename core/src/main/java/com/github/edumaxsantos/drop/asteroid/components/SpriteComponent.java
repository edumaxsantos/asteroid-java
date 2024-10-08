package com.github.edumaxsantos.drop.asteroid.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteComponent implements Component {
    private final Sprite sprite;

    public SpriteComponent(Texture texture) {
        this.sprite = new Sprite(texture);
        sprite.setOriginCenter();
    }

    public Sprite getSprite() {
        return sprite;
    }
}
