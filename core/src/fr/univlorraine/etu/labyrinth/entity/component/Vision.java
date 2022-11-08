package fr.univlorraine.etu.labyrinth.entity.component;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

public final class Vision implements Component {

    private final Circle value;

    public Vision(float x, float y, float radius) {
        this.value = new Circle(x, y, radius);
    }

    public Circle getValue() {
        return this.value;
    }

}
