package fr.univlorraine.etu.labyrinth.entity.component;

public final class Velocity implements Component {

    private final float value;

    public Velocity(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }
}
