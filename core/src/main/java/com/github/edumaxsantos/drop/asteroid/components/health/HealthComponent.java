package com.github.edumaxsantos.drop.asteroid.components.health;

import com.badlogic.ashley.core.Component;

public class HealthComponent implements Component {
    public float health;
    public final float maxHealth;

    public HealthComponent(int initialHealth) {
        this.health = initialHealth;
        this.maxHealth = initialHealth;
    }
}
