package com.mygdx.labyrinth;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.ScreenUtils;

public class Labyrinth extends ApplicationAdapter {

	private OrthographicCamera camera;
	// Permet de rendre les images ou les textures
	private SpriteBatch batch;
	// Nombre de colonne et de ligne dans l'image de base
	private static final int FRAME_COLS = 3, FRAME_ROWS = 4;
	// Héros animé
	private Animation<TextureRegion> walkAnimation;
	private Texture walkSheet;

	// Variable pour tracker le temps écoulé pour l'animation
	private float stateTime;

	@Override
	public void create () {
		batch = new SpriteBatch();
		// Equivalent à rectangle de 20m par 15m
		camera = new OrthographicCamera(20f,15f);
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
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		camera.update();

		stateTime += Gdx.graphics.getDeltaTime();

		Sprite currentFrame = new Sprite(walkAnimation.getKeyFrame(stateTime, true));
		// héros 1m de large et 1.8m de haut;
		currentFrame.setSize(1f, 1.8f);
		currentFrame.setPosition(3f, 3f);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		currentFrame.draw(batch);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		walkSheet.dispose();
	}
}
