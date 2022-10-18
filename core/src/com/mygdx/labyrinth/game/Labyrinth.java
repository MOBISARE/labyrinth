package com.mygdx.labyrinth.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.labyrinth.game.hud.HUD;
import com.mygdx.labyrinth.model.level.Level0;

import java.util.ArrayList;
import java.util.List;

public class Labyrinth extends Game {

	private final List<Screen> levels;

	private SpriteBatch batch;

	private HUD hud;


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
		this.setScreen(this.levels.get(0));

		//TODO Basculer d'écran de levels
	}

	@Override
	public void render() {
		super.render();
		batch.begin();
		hud.draw(this.batch);
		batch.end();
	}

	@Override
	public void dispose() {
		super.dispose();
		this.levels.forEach(Screen::dispose);
		this.batch.dispose();
		this.hud.dispose();
	}

	public List<Screen> getLevels() {
		return levels;
	}

	public SpriteBatch getBatch() {
		return batch;
	}
}
