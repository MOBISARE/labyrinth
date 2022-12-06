package com.mygdx.labyrinth.entity.component.collisions;

import com.mygdx.labyrinth.entity.component.Component;

public final class DynamicBody implements Component {

    private boolean value;

    public DynamicBody() {
        this.value = true;
    }

    public boolean isDynamic() {
        return value;
    }
}
