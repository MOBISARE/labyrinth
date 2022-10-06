package com.mygdx.labyrinth.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.labyrinth.worldl.WorldL;

public class GameScreen implements Screen {

    private final OrthographicCamera camera;

    // Variable pour tracker le temps écoulé pour l'animation
    private float stateTime;

    private final Labyrinth game;

    private float accumulator;

    private WorldL world;

    private final static float WIDTH_UNIT = 26f;


    public GameScreen(Labyrinth g) {
        this.game = g;
        this.world = new WorldL();
        world.initWorld();
        //camera = new OrthographicCamera(WIDTH_UNIT, (WIDTH_UNIT * Gdx.graphics.getHeight()) / Gdx.graphics.getWidth());
        camera = new OrthographicCamera(22f, 21.9f);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        stateTime = 0f;
        accumulator = 0f;
    }

    @Override
    public void render(float delta) {

        stateTime += Gdx.graphics.getDeltaTime();

        // fixed time step
        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(delta, 0.25f);
        accumulator += frameTime;
        while (accumulator >= 1/60f) {
            ScreenUtils.clear(0, 0, 0, 1);
            camera.update();

            game.getBatchPrincipal().setProjectionMatrix(camera.combined);
            game.getBatchPrincipal().begin();

            world.update(stateTime);
            world.draw(game.getBatchPrincipal(), stateTime);

            game.getBatchPrincipal().end();
            accumulator -= 1/60f;
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        world.dispose();
    }
}
