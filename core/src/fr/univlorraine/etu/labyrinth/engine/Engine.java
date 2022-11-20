package fr.univlorraine.etu.labyrinth.engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.univlorraine.etu.labyrinth.entity.Entity;
import fr.univlorraine.etu.labyrinth.entity.component.FollowingCamera;
import fr.univlorraine.etu.labyrinth.input.Cursor;
import fr.univlorraine.etu.labyrinth.input.GamePadAction;
import fr.univlorraine.etu.labyrinth.input.InputManager;
import fr.univlorraine.etu.labyrinth.entity.EntityManager;
import fr.univlorraine.etu.labyrinth.screen.Level1Screen;

public final class Engine extends Game {

    public static final float WIDTH = 20f;

    public static final float HEIGHT = 18f;

    public static final float SCALE = 4;

    public static final int TILE_SIZE = 16;

    private final EntityManager entityManager;

    private final InputManager inputManager;

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
        this.inputManager.assignKey(Input.Keys.DOWN, GamePadAction.DOWN);
        this.inputManager.assignKey(Input.Keys.LEFT, GamePadAction.LEFT);
        this.inputManager.assignKey(Input.Keys.RIGHT, GamePadAction.RIGHT);
        this.inputManager.assignKey(Input.Keys.SPACE, GamePadAction.ATTACK);
        this.inputManager.assignKey(Input.Keys.P, GamePadAction.PAUSE);
        this.inputManager.assignKey(Input.Keys.ESCAPE, GamePadAction.EXIT);

        //this.inputManager.assignCursor(0, CursorAction.ATTACK);
        Gdx.input.setInputProcessor(this.inputManager);
        this.setScreen(new Level1Screen(this));
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
