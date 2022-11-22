package fr.univlorraine.etu.labyrinth.entity.component;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public final class AnimatedSprite implements Component {

    private final Texture texture;

    private final Animation<TextureRegion> animation;

    private float stateTime;

    public AnimatedSprite(
            Texture texture,
            int columns,
            int rows,
            float frameDuration) {

        this.texture = texture;
        TextureRegion[][] regions = TextureRegion.split(
                this.texture,
                this.texture.getWidth() / columns,
                this.texture.getHeight() / rows
        );

        TextureRegion[] frames = new TextureRegion[columns * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                frames[index++] = regions[i][j];
            }
        }
        this.animation = new Animation<>(frameDuration, frames);
        this.stateTime = 0f;
    }

    public void update(float deltaTime) {
        this.stateTime += deltaTime;
    }

    public TextureRegion getFrame(boolean loop) {
        return this.animation.getKeyFrame(this.stateTime, loop);
    }

}
