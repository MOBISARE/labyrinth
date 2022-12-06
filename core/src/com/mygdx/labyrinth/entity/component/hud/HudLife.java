package com.mygdx.labyrinth.entity.component.hud;

import com.mygdx.labyrinth.entity.component.Component;
import com.mygdx.labyrinth.entity.component.sprite.StaticSprite;

public class HudLife implements Component {

    private final StaticSprite[] hearts;
    private final float height;
    private final float width;

    public HudLife(StaticSprite[] hearts, float w, float h) {
        this.hearts = hearts;
        this.width = w;
        this.height = h;
    }

    public StaticSprite getFullHeart() {
        return hearts[0];
    }

    public StaticSprite getHalfHeart() {
        return hearts[1];
    }

    public StaticSprite getEmptyHeart() {
        return hearts[2];
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
