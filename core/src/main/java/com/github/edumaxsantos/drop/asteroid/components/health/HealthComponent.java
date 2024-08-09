package com.github.edumaxsantos.drop.asteroid.components.health;

import com.badlogic.ashley.core.Component;

public class HealthComponent implements Component {
    private float health;
    public final float maxHealth;

    public HealthComponent(int initialHealth) {
        this.health = initialHealth;
        this.maxHealth = initialHealth;
    }

    public void reduceHealth(float amount) {
        health -= amount;
        if (health < 0) {
            health = 0;
        }
    }

    public void increaseHealth(float amount) {
        health += amount;
        if (health > maxHealth) {
            health = maxHealth;
        }
    }

    public boolean isDead() {
        return health == 0;
    }

    public float getHealth() {
        return health;
    }
}
