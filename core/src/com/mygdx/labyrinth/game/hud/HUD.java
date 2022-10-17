package com.mygdx.labyrinth.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.labyrinth.controller.Observer;
import com.mygdx.labyrinth.game.level.Level0;
import com.mygdx.labyrinth.model.Hero;

import java.awt.*;

/**
 * Classe qui permet d'afficher l'HUD et les informations importantes
 * sur le héro (la vie / le score)
 */
public class HUD implements Observer {

    private Level0 level;
    private Texture coeurPlein;
    private Texture coeurVide;
    private Texture coeurHalf;
    private Texture coin;
    private BitmapFont policeHud;

    private float x, y;
    private final int VIE_MAX = 6;
    private  int vieActuel;

    public HUD(Level0 l) {
        this.level = l;
        initHud();
    }

    private void initHud() {
        this.coeurPlein = new Texture(Gdx.files.internal("textures/ui_heart_full.png"));
        this.coeurHalf = new Texture(Gdx.files.internal("textures/ui_heart_half.png"));
        this.coeurVide = new Texture(Gdx.files.internal("textures/ui_heart_empty.png"));
        this.x = 0;
        vieActuel = level.getHero().getVie();

        // Initialisation de la police d'écriture
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/alagard.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        parameter.borderWidth = 0.8f;
        parameter.color = Color.YELLOW;
        policeHud = generator.generateFont(parameter);
        policeHud.setUseIntegerPositions(false);
        policeHud.getData().setScale(1/30f);
        generator.dispose();

        this.coin = new Texture(Gdx.files.internal("textures/coin_anim_f0.png"));

    }

    public void draw(Batch batch) {
        int pos = 0;

        // Dessin des coeurs plein
        for (int i = 0; i < vieActuel / 2; i++) {
            batch.draw(coeurPlein, level.getCamera().position.x - level.getCamera().viewportWidth / 2f + 0.2f + pos,
                    level.getCamera().position.y + level.getCamera().viewportHeight/2f - 1.2f,
                    0.9f,
                    0.9f);
            pos++;
        }

        // Dessin des demi-coeur
        if (vieActuel % 2 > 0) {
            batch.draw(coeurHalf, level.getCamera().position.x - level.getCamera().viewportWidth / 2f + 0.2f + pos,
                    level.getCamera().position.y + level.getCamera().viewportHeight/2f - 1.2f,
                    0.9f,
                    0.9f);
            pos++;
        }

        // Dessin des coeurs vide
        for (int i = 0; i < (VIE_MAX - vieActuel) / 2; i++) {
            batch.draw(coeurVide, level.getCamera().position.x - level.getCamera().viewportWidth / 2f + 0.2f + pos,
                    level.getCamera().position.y + level.getCamera().viewportHeight/2f - 1.2f,
                    0.9f,
                    0.9f);
            pos++;
        }

        batch.draw(coin, level.getCamera().position.x - level.getCamera().viewportWidth / 2f + 4f,
                level.getCamera().position.y + level.getCamera().viewportHeight / 2f - 1f,
                0.7f,0.7f);


        policeHud.draw(batch, level.getHero().getArgent() +"", level.getCamera().position.x - level.getCamera().viewportWidth / 2f + 5f,
                level.getCamera().position.y + level.getCamera().viewportHeight / 2f - 0.5f);
    }

    @Override
    public void update() {
        this.vieActuel = level.getHero().getVie();
    }

    public void dispose() {
        this.coeurPlein.dispose();
        this.coeurHalf.dispose();
        this.coeurVide.dispose();
        this.policeHud.dispose();
        this.coin.dispose();
    }
}
