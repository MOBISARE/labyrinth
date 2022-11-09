package fr.univlorraine.etu.labyrinth.entity.component;

public final class DynamicBody implements Component{

    private boolean value;

    public DynamicBody() {
        this.value = true;
    }

    public boolean isDynamic() {
        return value;
    }
}
