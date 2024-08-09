package com.github.edumaxsantos.drop.asteroid.components;

import com.badlogic.ashley.core.Component;

public class DamageComponent implements Component {
    public final float damage;

    public DamageComponent(float damage) {
        this.damage = damage;
    }
}
