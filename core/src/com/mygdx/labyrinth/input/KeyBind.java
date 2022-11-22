package com.mygdx.labyrinth.input;

import java.util.Objects;

public final class KeyBind {

    private final int keyCode;

    private final Bindable action;

    public KeyBind(int keyCode, Bindable action) {
        this.keyCode = keyCode;
        this.action = action;
    }

    @Override
    public boolean equals(Object o) {
        boolean equality;
        if (this == o) {
            equality = true;
        } else if (o == null || getClass() != o.getClass()) {
            equality = false;
        } else {
            KeyBind other = (KeyBind) o;
            equality = action == other.action;
        }
        return equality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(action);
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("KeyBind{");
        sb.append("keyCode=").append(keyCode);
        sb.append(", action=").append(action);
        sb.append('}');
        return sb.toString();
    }

    public int getKeyCode() {
        return keyCode;
    }

    public Bindable getAction() {
        return action;
    }
}
