package fr.univlorraine.etu.labyrinth.entity.component;

import com.badlogic.gdx.math.Rectangle;

public final class HitBox implements Component {

    private final Rectangle value;

    public HitBox(float x, float y, float w, float h) {
        this.value = new Rectangle(x, y, w, h);
    }

    public Rectangle getValue() {
        return value;
    }
}
