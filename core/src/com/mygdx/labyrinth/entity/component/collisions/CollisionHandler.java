package com.mygdx.labyrinth.entity.component.collisions;

import com.mygdx.labyrinth.entity.Entity;

public interface CollisionHandler {

    void handleCollision(Entity e1, Entity e2);
}
