package com.mygdx.labyrinth.entity.component.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.labyrinth.entity.component.Component;

public final class StaticSprite implements Component {

    private final TextureRegion textureRegion;

    public StaticSprite(Texture texture) {
        this.textureRegion = new TextureRegion(texture);
    }

    public boolean isFlipX() {
        return textureRegion.isFlipX();
    }

    public void flip(boolean flipX, boolean flipY) {
        boolean fx;
        boolean fy;
        if (this.isFlipX() != flipX) {
            fx = flipX;
        } else {
            fx = false;
        }
        if (this.isFlipY() != flipY) {
            fy = flipY;
        } else {
            fy = false;
        }

        this.textureRegion.flip(fx, fy);
    }

    public boolean isFlipY() {
        return textureRegion.isFlipY();
    }


    public Texture getTexture() {
        return this.textureRegion.getTexture();
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }
}
