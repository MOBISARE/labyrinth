package com.mygdx.labyrinth.entity.component;

import com.badlogic.gdx.math.Vector2;

public final class Position implements Component {
    private final Vector2 value;

    public Position(float x, float y) {
        this.value = new Vector2(x, y);
    }

    public Vector2 getValue() {
        return value;
    }
}
