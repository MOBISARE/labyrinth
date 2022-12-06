package com.mygdx.labyrinth.engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.labyrinth.entity.Entity;
import com.mygdx.labyrinth.entity.component.camera.FollowingCamera;
import com.mygdx.labyrinth.input.GamePadAction;
import com.mygdx.labyrinth.input.InputManager;
import com.mygdx.labyrinth.entity.EntityManager;
import com.mygdx.labyrinth.screen.Level1Screen;
import com.mygdx.labyrinth.screen.StartScreen;

public final class Engine extends Game {

    public static final float WIDTH = 20f;

    public static final float HEIGHT = 18f;

    public static final float SCALE = 4;

    public static final int TILE_SIZE = 16;

    private EntityManager entityManager;

    private InputManager inputManager;

    private SpriteBatch batch;

    public Engine() {
        this.entityManager = new EntityManager();
        this.inputManager = new InputManager();
    }

    @Override
    public void create() {
        // init screen par défaut.
        this.batch = new SpriteBatch();
        this.inputManager.assignKey(Input.Keys.UP, GamePadAction.UP);
        this.inputManager.assignKey(Input.Keys.W, GamePadAction.W);
        this.inputManager.assignKey(Input.Keys.DOWN, GamePadAction.DOWN);
        this.inputManager.assignKey(Input.Keys.S, GamePadAction.S);
        this.inputManager.assignKey(Input.Keys.LEFT, GamePadAction.LEFT);
        this.inputManager.assignKey(Input.Keys.A, GamePadAction.A);
        this.inputManager.assignKey(Input.Keys.RIGHT, GamePadAction.RIGHT);
        this.inputManager.assignKey(Input.Keys.D, GamePadAction.D);
        this.inputManager.assignKey(Input.Keys.SPACE, GamePadAction.ATTACK);
        this.inputManager.assignKey(Input.Keys.P, GamePadAction.PAUSE);
        this.inputManager.assignKey(Input.Keys.ESCAPE, GamePadAction.EXIT);
        this.inputManager.assignKey(Input.Keys.C, GamePadAction.DRAW_HITBOX);
        this.inputManager.assignKey(Input.Keys.ENTER, GamePadAction.PLAY);
        this.inputManager.assignKey(Input.Keys.TAB, GamePadAction.QUIT);

        //this.inputManager.assignCursor(0, CursorAction.ATTACK);
        Gdx.input.setInputProcessor(this.inputManager);
        this.setScreen(new StartScreen(this));
    }

    public void clear() {
        this.entityManager = new EntityManager();
        this.inputManager = new InputManager();
        this.create();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public OrthographicCamera getCamera() {
        return this.entityManager
                .findByNameAndComponent("camera", FollowingCamera.class)
                .getCamera();
    }

    public Entity getEntityByName(String name) {
        return this.entityManager.findByName(name);
    }
}
