package com.mygdx.labyrinth.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.labyrinth.controller.Observer;
import com.mygdx.labyrinth.model.Hero;

/**
 * Gère l'affiche de l'argent sur le hud
 */
public class ArgentHud implements Observer {

    //region Attributs

    /**
     * Texture d'une pièce
     */
    private final Texture coin;

    /**
     * Police utilisée pour afficher l'argent
     */
    private final BitmapFont policeHud;

    /**
     * Contient l'argent actuel du héro
     */
    private String argentActuel;

    //endregion

    /**
     * Constructeur par défaut
     * @param argent argent de départ
     */
    public ArgentHud(String argent) {
        this.argentActuel = argent;
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

    /**
     * {@inheritDoc}
     * @param obj données de mise à jour
     */
    @Override
    public void update(Object obj) {
        argentActuel = ((Hero)obj).getGold() + "";
    }

    /**
     * Affiche dans le hud l'argent
     * @param batch utilisé pour rendre les textures
     * @param x float
     * @param y float
     */
    public void draw(Batch batch, float x, float y) {
        batch.draw(coin, x + 4f, y - 1f, 0.7f,0.7f);
        policeHud.draw(batch, argentActuel +"", x + 5f, y - 0.5f);
    }

    /**
     * Libère les ressources
     */
    public void dispose() {
        this.coin.dispose();
        this.policeHud.dispose();
    }
}
