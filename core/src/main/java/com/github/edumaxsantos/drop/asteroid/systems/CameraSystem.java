package com.github.edumaxsantos.drop.asteroid.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.github.edumaxsantos.drop.asteroid.components.CameraComponent;
import com.github.edumaxsantos.drop.asteroid.components.PositionComponent;
import com.github.edumaxsantos.drop.asteroid.components.TransformComponent;

public class CameraSystem extends IteratingSystem {

    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<CameraComponent> cm;

    public CameraSystem() {
        super(Family.all(CameraComponent.class).get());

        tm = ComponentMapper.getFor(TransformComponent.class);
        cm = ComponentMapper.getFor(CameraComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        var cam = cm.get(entity);

        if (cam.target == null) {
            return;
        }

        var target = tm.get(cam.target);

        if (target == null)  {
            return;
        }

        cam.camera.position.y = Math.max(cam.camera.position.y, target.position.y);
    }
}
