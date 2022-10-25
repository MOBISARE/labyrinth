package com.mygdx.labyrinth.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.labyrinth.controller.InputProcessorHero;
import com.mygdx.labyrinth.exception.LabyrinthException;
import com.mygdx.labyrinth.game.hud.HUD;
import com.mygdx.labyrinth.model.level.Level0;
import com.mygdx.labyrinth.model.collision.CollisionManager;


import java.util.ArrayList;
import java.util.List;

public class Labyrinth extends Game {

	private final List<Screen> levels;

	private SpriteBatch batch;

	private HUD hud;
	private CollisionManager collisionManager;
	private boolean enModeDebug = false;



	public Labyrinth() {
		this.levels = new ArrayList<>();
	}

	@Override
	public void create() {
		// Sélecteur de niveaux
		this.batch = new SpriteBatch();
		Level0 level0 = new Level0(this);
		this.hud = new HUD();
		this.hud.setEvenType(level0);
		this.levels.add(level0);


		InputProcessorHero inputProcessorHero = new InputProcessorHero(level0.getHero(), this);
		Gdx.input.setInputProcessor(inputProcessorHero);

		this.collisionManager = new CollisionManager(level0);

		try {
			this.collisionManager.init();
		} catch (LabyrinthException e) {
			throw new RuntimeException(e);
		}


		this.setScreen(this.levels.get(0));

		//TODO Basculer d'écran de levels
	}

	@Override
	public void render() {
		super.render();
		collisionManager.step();
		batch.begin();
		hud.draw(this.batch);
		batch.end();

		if (enModeDebug) {
			collisionManager.debugRenderer(((Level0) levels.get(0)).getCamera());
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		this.levels.forEach(Screen::dispose);
		this.batch.dispose();
		this.hud.dispose();
		this.collisionManager.dispose();
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	/**
	 * Allume ou éteints le mode débug
	 */
	public void toggleModeDebug() {
		enModeDebug = !enModeDebug;
	}
}
