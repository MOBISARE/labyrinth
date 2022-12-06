package com.mygdx.labyrinth.entity.component.movement;

import com.mygdx.labyrinth.entity.component.Component;

public final class Velocity implements Component {

    private final float value;

    public Velocity(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }
}
