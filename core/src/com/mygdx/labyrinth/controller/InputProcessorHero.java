package com.mygdx.labyrinth.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.labyrinth.game.Labyrinth;
import com.mygdx.labyrinth.model.Hero;

/**
 * Gestion principal des déplacements du héro
 */
public class InputProcessorHero implements InputProcessor {

    private final Hero hero;
    private final Labyrinth game;

    public InputProcessorHero(Hero h, Labyrinth g) {
        this.hero = h;
        this.game = g;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                hero.setMovingLeft(true);
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                hero.setMovingRight(true);
                break;
            case Input.Keys.W:
            case Input.Keys.UP:
                hero.setMovingUp(true);
                break;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                hero.setMovingDown(true);
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                hero.setMovingLeft(false);
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                hero.setMovingRight(false);
                break;
            case Input.Keys.W:
            case Input.Keys.UP:
                hero.setMovingUp(false);
                break;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                hero.setMovingDown(false);
                break;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if (character == 'p') {
            hero.setHealthPoint(hero.getHealthPoint() - 1);
        }
        if (character == '+') {
            hero.addArgent(1);
        }
        if (character == '-') {
            hero.addArgent(-1);
        }
        if (character == 'c') {
            game.toggleModeDebug();
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
