package com.mygdx.labyrinth.entity.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Font implements Component {
    private final BitmapFont policeHud;

    public Font(String path, int size, float borderWidth, Color color, float scale) {
        // Initialisation de la police d'écriture
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size; //16;
        parameter.borderWidth = borderWidth; //0.8f;
        parameter.color = color; //Color.YELLOW;
        policeHud = generator.generateFont(parameter);
        policeHud.setUseIntegerPositions(false);
        policeHud.getData().setScale(scale); //1/30f);
        generator.dispose();
    }

    public BitmapFont getFont() {
        return this.policeHud;
    }
}
