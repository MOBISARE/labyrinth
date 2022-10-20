package com.mygdx.labyrinth.model.collision;

/**
 * Classe qui représente un événement de collision
 */
public class CollisionEvent {

    //region Attributs

    /**
     * Element qui rentrent en collision
     */
    private final Body body1;
    private final Body body2;

    //endregion

    /**
     * Constructeur par défaut
     * @param b1 Body
     * @param b2 Body
     */
    public CollisionEvent(Body b1, Body b2) {
        this.body1 = b1;
        this.body2 = b2;
    }

    /**
     * Retourne le body qui rentre en collision
     * @return Body
     */
    public Body getBody1() {
        return body1;
    }

    /**
     * Retourne le body qui se faire rentrer en collision
     * @return Body
     */
    public Body getBody2() {
        return body2;
    }
}
