package fr.univlorraine.etu.labyrinth.entity.component;

public final class Trajectory implements Component {

    private final float startX;

    private final float startY;

    private final float angle;

    private final float distance;

    public Trajectory(float startX, float startY, float angle, float distance) {
        this.startX = startX;
        this.startY = startY;
        this.angle = angle;
        this.distance = distance;
    }

    public float getStartX() {
        return startX;
    }

    public float getStartY() {
        return startY;
    }

    public float getAngle() {
        return angle;
    }

    public float getDistance() {
        return distance;
    }
}
