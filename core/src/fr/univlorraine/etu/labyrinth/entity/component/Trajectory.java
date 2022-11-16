package fr.univlorraine.etu.labyrinth.entity.component;

import com.badlogic.gdx.math.Vector2;

public final class Trajectory implements Component {

    private final Vector2 direction;

    private final float distance;

    public Trajectory(Vector2 direction, float distance) {
        this.direction = direction;
        this.distance = distance;
    }

    public float getStartX() {
        return direction.x;
    }

    public float getStartY() {
        return direction.y;
    }

    public float getAngle() {
        return direction.angleDeg();
    }

    public float getDistance() {
        return distance;
    }

    public Vector2 getVector() {
        return direction;
    }
}
