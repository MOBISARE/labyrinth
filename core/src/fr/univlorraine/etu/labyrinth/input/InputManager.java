package fr.univlorraine.etu.labyrinth.input;

import com.badlogic.gdx.InputProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class InputManager implements InputProcessor {

    private final Map<KeyBind, Boolean> keys;

    private final Cursor cursor;

    public InputManager() {
        this.keys = new HashMap<>();
        this.cursor = new Cursor();
    }

    public boolean isPressed(GamePadAction action) {
//        boolean pressed = false;
//        for (Map.Entry<KeyBind, Boolean> entry : this.keys.entrySet()) {
//            KeyBind k = entry.getKey();
//            Boolean v = entry.getValue();
//            if (Objects.equals(k.getAction(), action)) {
//                pressed = v;
//                break;
//            }
//        }
//        return pressed;

       return this.keys.entrySet()
                .stream()
                .filter(e -> Objects.equals(e.getKey().getAction(), action))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(Boolean.FALSE);
    }

    public void assignKey(int keycode, GamePadAction action) {
        this.keys.put(new KeyBind(keycode, action), false);
    }

    public void assignCursor(int button, CursorAction action) {
        this.cursor.keys.put(new KeyBind(button, action), false);
    }

    @Override
    public boolean keyDown(int keycode) {
        return processKey(keycode, true);
    }

    @Override
    public boolean keyUp(int keycode) {
        return processKey(keycode, false);
    }

    private boolean processKey(int keycode, boolean pressed) {
        Optional<KeyBind> option = this.keys
                .keySet()
                .stream()
                .filter(e -> Objects.equals(keycode, e.getKeyCode()))
                .findFirst();

        boolean pushed;
        if (option.isPresent()) {
            this.keys.put(option.get(), pressed);

            pushed = true;
        } else {
            pushed = false;
        }
        return pushed;
    }
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    private boolean processCursor(int button, boolean pressed) {
        Optional<KeyBind> option = this.cursor.keys
                .keySet()
                .stream()
                .filter(e -> Objects.equals(button, e.getKeyCode()))
                .findFirst();

        boolean pushed;
        if (option.isPresent()) {
            this.cursor.keys.put(option.get(), pressed);
            this.cursor
                    .lastClickPosition
                    .put((CursorAction) option
                            .get()
                            .getAction(),
                            new ClickPosition(this.cursor.x, this.cursor.y));
            pushed = true;
        } else {
            pushed = false;
        }
        return pushed;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        processCursor(button, true);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        processCursor(button, false);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        this.cursor.x = screenX;
        this.cursor.y = screenY;
        //System.out.println("Position (X :" + this.cursor.x + "; Y : "  + this.cursor.y + ")");
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    public Cursor getCursor() {
        return cursor;
    }
}

