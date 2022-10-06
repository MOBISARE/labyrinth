package com.mygdx.labyrinth.worldl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.labyrinth.inputProcessor.InputProcessorHero;

import java.util.ArrayList;
import java.util.List;

public class WorldL {

    private Hero hero;
    /**
     * Contient la liste des éléments composants le monde
     */
    private List<WorldObject> worldObjects;

    /**
     * Classe principal qui contient l'ensemble des élément du jeux et qui permet
     * de gérer la physique
     */
    public WorldL() {
        this.worldObjects = new ArrayList<>(50);
    }

    /**
     * Initialise le monde
     */
    public void initWorld() {
        this.hero = new Hero(0f,0f,1f,1.8f);
        InputProcessorHero inputProcessorHero = new InputProcessorHero(hero);
        Gdx.input.setInputProcessor(inputProcessorHero);

        worldObjects.add(hero);
    }

    /**
     * Met à jour les données de tous les éléments à chaque itération
     * @param deltaTime
     */
    public void update(float deltaTime) {
        for (WorldObject obj: worldObjects) {
            obj.update(deltaTime);
        }
    }

    /**
     * Dessine à l'écran l'ensemble des éléments
     * ATTENTION ! faire bien attention à l'orde d'ajout des éléments dans la liste : les éléments dessinés en dernier
     * son ceux affiché le plus en avant
     * @param batch
     * @param deltaTime
     */
    public void draw(SpriteBatch batch, float deltaTime) {
        for (WorldObject obj: worldObjects) {
            obj.draw(batch, deltaTime);
        }
    }

    public void dispose() {
        for (WorldObject obj: worldObjects) {
            obj.dispose();
        }
    }
}
