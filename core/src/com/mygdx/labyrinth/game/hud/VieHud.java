package com.mygdx.labyrinth.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.labyrinth.controller.Observer;
import com.mygdx.labyrinth.model.Hero;

/**
 * Module du hud qui gère la vie
 */
public class VieHud implements Observer {

    //region Attributs

    /**
     * Texture d'un coeur plein
     */
    private final Texture coeurPlein;

    /**
     * Texture d'un coeur vide
     */
    private final Texture coeurVide;

    /**
     * Texture d'une moitié de coeur
     */
    private final Texture coeurHalf;

    /**
     * Vie max que peut posséder le héro (surement pas au bon endroit)
     */
    private final int VIE_MAX = 6;

    /**
     * Vie actuel du héro
     */
    private  int vieActuel;

    //endregion

    /**
     * Vonstructeur
     * @param vie de départ
     */
    public VieHud(int vie) {
        this.vieActuel = vie;
        this.coeurPlein = new Texture(Gdx.files.internal("textures/ui_heart_full.png"));
        this.coeurHalf = new Texture(Gdx.files.internal("textures/ui_heart_half.png"));
        this.coeurVide = new Texture(Gdx.files.internal("textures/ui_heart_empty.png"));
    }

    /**
     * {@inheritDoc}
     * @param obj données de mise à jour
     */
    @Override
    public void update(Object obj) {
        vieActuel = ((Hero) obj).getHealthPoint();
    }

    /**
     * Permet de dessiner à l'écran cette partie de hud
     * @param batch Batch
     * @param x float
     * @param y float
     */
    public void draw(Batch batch, float x, float y) {
        int pos = 0;

        // Dessin des coeurs plein
        for (int i = 0; i < vieActuel / 2; i++) {
            batch.draw(coeurPlein, x + 0.2f + pos, y - 1.2f, 0.9f, 0.9f);
            pos++;
        }

        // Dessin des demi-coeur
        if (vieActuel % 2 > 0) {
            batch.draw(coeurHalf, x+ 0.2f + pos, y - 1.2f, 0.9f, 0.9f);
            pos++;
        }

        // Dessin des coeurs vide
        for (int i = 0; i < (VIE_MAX - vieActuel) / 2; i++) {
            batch.draw(coeurVide, x + 0.2f + pos, y - 1.2f, 0.9f, 0.9f);
            pos++;
        }
    }

    /**
     * Libère les ressources
     */
    public void dispose() {
        this.coeurPlein.dispose();
        this.coeurHalf.dispose();
        this.coeurVide.dispose();
    }
}
