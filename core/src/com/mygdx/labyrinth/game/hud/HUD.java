package com.mygdx.labyrinth.game.hud;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.labyrinth.controller.Observer;
import com.mygdx.labyrinth.model.level.Level0;

/**
 * Classe qui permet d'afficher l'HUD et les informations importantes
 * sur le héro (la vie / le score)
 */
public class HUD implements Observer{

    //region Attributs

    /**
     * Position en haut à gauche de l'HUD
     */
    private float x, y;
    /**
     * Hud qui gère l'argent
     */
    private final ArgentHud argentHud;
    /**
     * Partie du hud qui gère la vie
     */
    private final VieHud vieHud;

    //endregion

    /**
     * Constructeur par défaut
     */
    public HUD() {
        this.x = 0;
        this.y = 0;
        this.argentHud = new ArgentHud("0");
        this.vieHud = new VieHud(6);
    }

    /**
     * Dessine le hud à l'écran
     * @param batch Batch
     */
    public void draw(Batch batch) {
        vieHud.draw(batch, x, y);
        argentHud.draw(batch, x, y);
    }

    /**
     * {@inheritDoc}
     * @param obj données de mise à jour
     */
    @Override
    public void update(Object obj) {
        x = ((OrthographicCamera)obj).position.x - ((OrthographicCamera)obj).viewportWidth / 2f;
        y = ((OrthographicCamera)obj).position.y + ((OrthographicCamera)obj).viewportHeight / 2f;
    }

    /**
     * Libère les ressources
     */
    public void dispose() {
        argentHud.dispose();
        vieHud.dispose();
    }

    /**
     * Permet de mettre en lien les observateurs et les observés
     * @param l Level0
     */
    public void setEvenType(Level0 l) {
        l.addObserver("camera_position_event", this);
        l.addObserver("vie_hero_event", vieHud);
        l.addObserver("argent_hero_event", argentHud);
    }
}
