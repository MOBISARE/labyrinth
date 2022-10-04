package com.mygdx.labyrinth;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {

    private final OrthographicCamera camera;
    // Permet de rendre les images ou les textures
    private static final int FRAME_COLS = 3, FRAME_ROWS = 4;
    // Héros animé
    private Animation<TextureRegion> walkAnimation;
    private Texture walkSheet;

    // Variable pour tracker le temps écoulé pour l'animation
    private float stateTime;

    private final Labyrinth game;

    public GameScreen(Labyrinth g) {
        this.game = g;
        camera = new OrthographicCamera(220,200);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        walkSheet = new Texture(Gdx.files.internal("heros.png"));
        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / FRAME_COLS,
                walkSheet.getHeight() / FRAME_ROWS);

        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }

        walkAnimation = new Animation<TextureRegion>(0.1f, walkFrames);

        stateTime = 0f;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();

        stateTime += Gdx.graphics.getDeltaTime();

        TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);

        game.getBatchPrincipal().setProjectionMatrix(camera.combined);
        game.getBatchPrincipal().begin();
        game.getBatchPrincipal().draw(currentFrame, 50, 50);
        game.getBatchPrincipal().end();
    }

    @Override
    public void dispose() {
        walkSheet.dispose();
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


}
