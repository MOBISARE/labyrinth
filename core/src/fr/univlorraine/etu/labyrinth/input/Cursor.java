package fr.univlorraine.etu.labyrinth.input;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Cursor {

    float x;

    float y;

    final Map<KeyBind, Boolean> keys;

    final Map<CursorAction, ClickPosition> lastClickPosition;

    Cursor() {
        this.x = 0;
        this.y = 0;
        this.keys = new HashMap<>();
        this.lastClickPosition = new EnumMap<>(CursorAction.class);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean isButtonClicked(CursorAction action) {
        return this.keys
                .entrySet()
                .stream().filter(e -> Objects.equals(e.getKey().getAction(), action))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(Boolean.FALSE);
    }

    public ClickPosition getLastPosition(CursorAction action) {
        return this.lastClickPosition.get(action);
    }
}
