package com.mygdx.labyrinth.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.labyrinth.controller.Observer;
import com.mygdx.labyrinth.game.level.Level0;
import com.mygdx.labyrinth.model.Hero;

/**
 * Classe qui permet d'afficher l'HUD et les informations importantes
 * sur le héro (la vie / le score)
 */
public class HUD implements Observer {

    private Level0 level;
    private Texture coeurPlein;

    private float x, y;

    public HUD(Level0 l) {
        this.level = l;
        initHud();
    }

    private void initHud() {
        this.coeurPlein = new Texture(Gdx.files.internal("textures/ui_heart_full.png"));
        this.x = 0;
    }

    public void draw(Batch batch) {
        batch.draw(coeurPlein, level.getCamera().position.x - level.getCamera().viewportWidth / 2f + 0.2f,
                level.getCamera().position.y + level.getCamera().viewportHeight/2f - 1.2f,
                0.9f,
                0.9f);
        batch.draw(coeurPlein, level.getCamera().position.x - level.getCamera().viewportWidth / 2f + 1.2f,
                level.getCamera().position.y + level.getCamera().viewportHeight/2f - 1.2f,
                0.9f,
                0.9f);
        batch.draw(coeurPlein, level.getCamera().position.x - level.getCamera().viewportWidth / 2f + 2.2f,
                level.getCamera().position.y + level.getCamera().viewportHeight/2f - 1.2f,
                0.9f,
                0.9f);
    }

    @Override
    public void update() {

    }

    public void dispose() {
        this.coeurPlein.dispose();
    }
}
