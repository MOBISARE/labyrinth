package com.mygdx.labyrinth.input;

import com.badlogic.gdx.math.Vector2;

public class Cursor {

    private boolean isPressed;
    private Vector2 position;

    public Cursor() {
        this.isPressed = false;
        this.position = new Vector2(0, 0);
    }

    public void setPressed(boolean pressed) {
        isPressed = pressed;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(int x, int y) {
        this.position.set(x, y);
    }
}
