package com.mygdx.labyrinth.entity.component.movement;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.labyrinth.entity.component.Component;

public final class Direction implements Component {

    private final Vector2 value;

    public Direction(float x, float y) {
        this.value = new Vector2(x, y);
    }

    public Vector2 getValue() {
        return value;
    }
}
