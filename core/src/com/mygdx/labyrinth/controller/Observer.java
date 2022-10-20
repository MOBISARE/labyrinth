package com.mygdx.labyrinth.controller;

/**
 * Intérface necessaire au design pattern Observer utilisé par le HUD
 */
public interface Observer {

    /**
     * Met a jour l'observateur
     * @param obj données de mise à jour
     */
    void update(Object obj);
}
