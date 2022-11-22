package com.mygdx.labyrinth.entity.component;

import com.badlogic.gdx.math.*;

public final class HitBox implements Component {

    private final Polygon box;
    private boolean isActive;
    private final boolean isDynamic;
    private final Vector2 oldPosition;
    private final float width;
    private final float height;

    public HitBox(float x, float y, float w, float h, boolean isActive, boolean isDynamic) {
        this.box = new Polygon(new float[]{0f, 0f,
                                            w, 0f,
                                            w, h,
                                            0f, h});
        this.isActive = isActive;
        this.isDynamic = isDynamic;
        this.oldPosition = new Vector2(0,0);
        height = h;
        width = w;
        this.box.setPosition(x, y);
    }

    public float getX() {
        return box.getX();
    }

    public float getY() {
        return box.getY();
    }

    public void setX(float x) {
        this.box.setPosition(x, this.box.getY());
    }

    public void setY(float y) {
        this.box.setPosition(box.getX(), y);
    }

    public void setPosition(float x, float y) {
        this.box.setPosition(x, y);
    }

    public Polygon getBox() {
        return box;
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

    public Vector2 getOldPosition() {
        return oldPosition;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }
}
