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

    // Position en haut à gauche de l'HUD
    private float x, y;
    private final ArgentHud argentHud;
    private final VieHud vieHud;

    public HUD() {
        this.x = 0;
        this.y = 0;
        this.argentHud = new ArgentHud("0");
        this.vieHud = new VieHud(6);
    }

    public void draw(Batch batch) {
        vieHud.draw(batch, x, y);
        argentHud.draw(batch, x, y);
    }

    @Override
    public void update(Object obj) {
        x = ((OrthographicCamera)obj).position.x - ((OrthographicCamera)obj).viewportWidth / 2f;
        y = ((OrthographicCamera)obj).position.y + ((OrthographicCamera)obj).viewportHeight / 2f;
    }

    public void dispose() {
        argentHud.dispose();
        vieHud.dispose();
    }

    public void setEvenType(Level0 l) {
        l.addObserver("camera_position_event", this);
        l.addObserver("vie_hero_event", vieHud);
        l.addObserver("argent_hero_event", argentHud);
    }
}
