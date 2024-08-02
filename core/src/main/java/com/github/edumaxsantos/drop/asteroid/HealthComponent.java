package com.github.edumaxsantos.drop.asteroid;

import com.badlogic.gdx.math.MathUtils;

public class HealthComponent {
    private final int maxHealth;
    private int currentHealth;

    public HealthComponent(int maxHealth) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void takeDamage(float damage) {
        currentHealth -= MathUtils.floor(damage);

        if (currentHealth < 0) {
            currentHealth = 0;
        }
    }

    public void heal(float amount) {
        currentHealth += MathUtils.ceil(amount);

        if (currentHealth > maxHealth) {
            currentHealth = maxHealth;
        }
    }


    public boolean isDead() {
        return currentHealth <= 0;
    }


}
