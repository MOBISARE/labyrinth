package com.mygdx.labyrinth.model;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class InputProcessorHero implements InputProcessor {

    private final Hero hero;

    public InputProcessorHero(Hero h) {
        this.hero = h;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                hero.setLeftMove(true);
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                hero.setRightMove(true);
                break;
            case Input.Keys.W:
            case Input.Keys.UP:
                hero.setUpMove(true);
                break;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                hero.setDownMove(true);
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                hero.setLeftMove(false);
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                hero.setRightMove(false);
                break;
            case Input.Keys.W:
            case Input.Keys.UP:
                hero.setUpMove(false);
                break;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                hero.setDownMove(false);
                break;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if (character == 'p') {
            hero.setVie(hero.getVie() - 1);
        }
        if (character == '+') {
            hero.addArgent(1);
        }
        if (character == '-') {
            hero.addArgent(-1);
        }
        return true;
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
