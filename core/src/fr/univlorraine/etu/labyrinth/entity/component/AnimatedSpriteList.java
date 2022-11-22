package fr.univlorraine.etu.labyrinth.entity.component;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class AnimatedSpriteList implements Component {

    private final Texture texture;

    private Map<String, Animation<TextureRegion>> animations;

    private String currentAnimationName;

    private float stateTime;

    private boolean flipX;

    private boolean flipY;

    public AnimatedSpriteList(
            Texture texture,
            int columns,
            int rows,
            Map<String, AnimationData> data) {

        this.texture = texture;
        this.animations = new HashMap<>();
        this.stateTime = 0f;
        this.flipX = false;
        this.flipY = false;
        this.currentAnimationName = data
                .keySet()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Pas d'animation par défaut"));

        TextureRegion[][] regions = TextureRegion.split(
                this.texture,
                this.texture.getWidth() / columns,
                this.texture.getHeight() / rows
        );

        for (Map.Entry<String, AnimationData> entry : data.entrySet()) {
            String animationName = entry.getKey();
            AnimationData animationData = entry.getValue();

            int size = (animationData.lastFrameIndex - animationData.firstFrameIndex) * rows;
            TextureRegion[] frames = new TextureRegion[size];
            int index = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = animationData.firstFrameIndex; j < animationData.lastFrameIndex; j++) {
                    frames[index++] = regions[i][j];
                }
            }
            Animation<TextureRegion> animation = new Animation<>(animationData.frameDuration, frames);
            this.animations.put(animationName, animation);
        }

    }

    public void update(float deltaTime) {
        this.stateTime += deltaTime;
    }

    public TextureRegion getFrame(boolean loop) {
        Animation<TextureRegion> animation = this.animations.get(this.currentAnimationName);
        return animation.getKeyFrame(this.stateTime, loop);
    }

    public String getCurrentAnimationName() {
        return currentAnimationName;
    }

    public void setCurrentAnimationName(String currentAnimationName) {
        if (!Objects.equals(this.currentAnimationName, currentAnimationName)) {
            this.stateTime = 0;
        }
        this.currentAnimationName = currentAnimationName;
    }

    public boolean isFlipX() {
        return flipX;
    }

    public void setFlipX(boolean flipX) {
            this.flipX = flipX;
    }

    public boolean isFlipY() {
        return flipY;
    }

    public void setFlipY(boolean flipY) {
        this.flipY = flipY;
    }

    public static class AnimationData {

        private final float frameDuration;

        private final int firstFrameIndex;

        private final int lastFrameIndex;

        public AnimationData(
                float frameDuration,
                int firstFrameIndex,
                int lastFrameIndex) {

            this.frameDuration = frameDuration;
            this.firstFrameIndex = firstFrameIndex;
            this.lastFrameIndex = lastFrameIndex;
        }
    }
}
