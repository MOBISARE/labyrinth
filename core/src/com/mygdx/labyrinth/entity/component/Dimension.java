package com.mygdx.labyrinth.entity.component;

public class Dimension implements Component{

    private float height;
    private float width;

    public Dimension(float h, float w) {
        this.height = h;
        this.width = w;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setWidth(float width) {
        this.width = width;
    }
}
