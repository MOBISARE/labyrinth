package fr.univlorraine.etu.labyrinth.entity.component;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;

public final class HitBox implements Component {

    private final Rectangle value;
    private boolean isActive;
    private boolean isDynamic;

    public HitBox(float x, float y, float w, float h, boolean isActive, boolean isDynamic) {
        this.value = new Rectangle(x, y, w, h);
        this.isActive = isActive;
        this.isDynamic = isDynamic;
    }

    public Rectangle getValue() {
        return value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HitBox{");
        sb.append("x= ").append(value.x).append(",");
        sb.append("y= ").append(value.y).append(",");
        sb.append("width= ").append(value.width).append(",");
        sb.append("height= ").append(value.height);
        sb.append('}');
        return sb.toString();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActity(boolean b) {
        this.isActive = b;
    }

    public boolean isDynamic() {
        return isDynamic;
    }
}
