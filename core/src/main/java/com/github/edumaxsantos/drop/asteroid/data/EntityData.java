package com.github.edumaxsantos.drop.asteroid.data;

import com.badlogic.ashley.core.Entity;
import com.github.edumaxsantos.drop.asteroid.enums.ElementType;

public class EntityData {
    public final ElementType type;
    public final Entity entity;

    public EntityData(ElementType type, Entity entity) {
        if (type == null) {
            throw new NullPointerException("type is null");
        }

        if (entity == null) {
            throw new NullPointerException("entity is null");
        }
        this.type = type;
        this.entity = entity;
    }

    public boolean is(ElementType type) {
        return this.type.equals(type);
    }
}
