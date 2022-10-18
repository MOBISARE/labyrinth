package com.mygdx.labyrinth.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.labyrinth.controller.Observer;
import com.mygdx.labyrinth.model.Hero;

public class VieHud implements Observer {

    private final Texture coeurPlein;
    private final Texture coeurVide;
    private final Texture coeurHalf;
    private final int VIE_MAX = 6;
    private  int vieActuel;

    public VieHud(int vie) {
        this.vieActuel = vie;
        this.coeurPlein = new Texture(Gdx.files.internal("textures/ui_heart_full.png"));
        this.coeurHalf = new Texture(Gdx.files.internal("textures/ui_heart_half.png"));
        this.coeurVide = new Texture(Gdx.files.internal("textures/ui_heart_empty.png"));
    }

    @Override
    public void update(Object obj) {
        vieActuel = ((Hero) obj).getVie();
    }

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

    public void dispose() {
        this.coeurPlein.dispose();
        this.coeurHalf.dispose();
        this.coeurVide.dispose();
    }
}
