package com.mygdx.labyrinth.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.*;
import java.util.function.Predicate;


public class EntityManager {

    private static final EntityManager SINGLETON = new EntityManager();

    private final List<Entity> entities;

    private EntityManager() {
        this.entities = new ArrayList<>();
    }

    public static EntityManager getInstance(){
        return SINGLETON;
    }

    public void add(Entity entity){
        this.entities.add(entity);
    }

    public void render(SpriteBatch batch, float deltaTime){
        this.entities.forEach(e -> e.render(batch, deltaTime));
    }

    public void dispose(){
        this.entities.forEach(Entity::dispose);
        this.entities.clear();
    }

    public Optional<Entity> findByName(String name){
        return this.entities
                .stream()
                .filter(e -> Objects.equals(e.getName(), name))
                .findAny();
    }

    public void removeIf(Predicate<Entity> predicate){
        this.entities.removeIf(predicate);
    }

    public List<Entity> values() {
        return List.copyOf(this.entities);
    }
}
