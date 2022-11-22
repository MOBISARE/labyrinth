package fr.univlorraine.etu.labyrinth.entity;

import fr.univlorraine.etu.labyrinth.entity.Entity;

import java.util.ArrayList;

public interface CollisionHandler {

    void handleCollision(Entity e1, Entity e2);
}
