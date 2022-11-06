package fr.univlorraine.etu.labyrinth.engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.univlorraine.etu.labyrinth.input.GamePadAction;
import fr.univlorraine.etu.labyrinth.input.InputManager;
import fr.univlorraine.etu.labyrinth.entity.EntityManager;
import fr.univlorraine.etu.labyrinth.screen.Level1Screen;

public final class Engine extends Game {

    public static final int WIDTH = 300;

    public static final int HEIGHT = WIDTH / 16 * 9;

    public static final int SCALE = 4;

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
        this.inputManager.assign(Input.Keys.UP, GamePadAction.UP);
        this.inputManager.assign(Input.Keys.DOWN, GamePadAction.DOWN);
        this.inputManager.assign(Input.Keys.LEFT, GamePadAction.LEFT);
        this.inputManager.assign(Input.Keys.RIGHT, GamePadAction.RIGHT);
        this.inputManager.assign(Input.Keys.SPACE, GamePadAction.ATTACK);
        this.inputManager.assign(Input.Keys.P, GamePadAction.PAUSE);
        this.inputManager.assign(Input.Keys.ESCAPE, GamePadAction.EXIT);

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
}
