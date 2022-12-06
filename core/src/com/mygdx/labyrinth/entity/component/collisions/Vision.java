package com.mygdx.labyrinth.entity.component.collisions;

import com.badlogic.gdx.math.Circle;
import com.mygdx.labyrinth.entity.component.Component;

public final class Vision implements Component {

    private final Circle value;

    public Vision(float x, float y, float radius) {
        this.value = new Circle(x, y, radius);
    }

    public Circle getValue() {
        return this.value;
    }

}
