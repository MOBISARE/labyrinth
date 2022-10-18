package com.mygdx.labyrinth.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.labyrinth.controller.Observer;
import com.mygdx.labyrinth.model.Hero;

public class ArgentHud implements Observer {
    private Texture coin;
    private BitmapFont policeHud;
    private String argentActuel;

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
    @Override
    public void update(Object obj) {
        argentActuel = ((Hero)obj).getArgent() + "";
    }

    public void draw(Batch batch, float x, float y) {
        batch.draw(coin, x + 4f, y - 1f, 0.7f,0.7f);
        policeHud.draw(batch, argentActuel +"", x + 5f, y - 0.5f);
    }

    public void dispose() {
        this.coin.dispose();
    }
}
