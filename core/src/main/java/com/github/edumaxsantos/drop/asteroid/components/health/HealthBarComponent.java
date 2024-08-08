package com.github.edumaxsantos.drop.asteroid.components.health;

import com.badlogic.ashley.core.Component;

public class HealthBarComponent implements Component {
    public float timeVisible;

    public HealthBarComponent() {
        this.timeVisible = 0;
    }
}
