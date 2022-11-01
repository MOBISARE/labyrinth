package com.mygdx.labyrinth.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.labyrinth.model.collision.Body;

/**
 * Interface représentant tous les éléments du jeu ajouté en parallèle de la map
 */
public interface Entity {

    /**
     * Permet d'afficher l'entité à l'écran
     * @param batch SpriteBatch, batch utilisé pour le rendu
     * @param deltaTime float, temps écoulé depuis la dernière image affichée
     */
    void render(SpriteBatch batch, float deltaTime);

    /**
     * Permet de libérer les ressources
     */
    void dispose();

    /**
     * Retourne le body de l'entité
     * @return Body's entity
     */
    Body getBody();

    /**
     * Permet de gérer la collision de l'entité avec l'élément b
     * @param b Body
     */
    void handleCollision(Body b);

    /**
     * Permet de savoir si l'entité est détruite ou pas
     */
    boolean isDestroyed();

    default String getName(){
        return "unknown";
    }
}
