package fr.univlorraine.etu.labyrinth.input;

import java.util.Objects;

public class ClickPosition {

    float x;

    float y;

    ClickPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClickPosition that = (ClickPosition) o;
        return Float.compare(that.x, x) == 0 && Float.compare(that.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ClickPosition{");
        sb.append("x = ").append(x);
        sb.append(", y = ").append(y);
        sb.append('}');
        return sb.toString();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
