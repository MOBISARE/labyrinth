package fr.univlorraine.etu.labyrinth.input;

import com.badlogic.gdx.InputProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class InputManager implements InputProcessor {

    private final Map<KeyBind, Boolean> keys;

    public InputManager() {
        this.keys = new HashMap<>();
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

    public void assign(int keycode, GamePadAction action) {
        this.keys.put(new KeyBind(keycode, action), false);
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

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
