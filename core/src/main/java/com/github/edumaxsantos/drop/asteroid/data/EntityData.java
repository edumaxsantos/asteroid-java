package com.github.edumaxsantos.drop.asteroid.data;

import com.badlogic.ashley.core.Entity;
import com.github.edumaxsantos.drop.asteroid.enums.ElementType;

public class EntityData {
    public ElementType type;
    public Entity entity;

    public EntityData(ElementType type, Entity entity) {
        this.type = type;
        this.entity = entity;
    }
}
