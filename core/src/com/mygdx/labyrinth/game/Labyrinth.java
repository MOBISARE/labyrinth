package com.mygdx.labyrinth.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Labyrinth extends Game {

	private SpriteBatch batchPrincipal;

	@Override
	public void create () {
		this.batchPrincipal = new SpriteBatch();
		this.setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		batchPrincipal.dispose();
	}

	public SpriteBatch getBatchPrincipal() {
		return this.batchPrincipal;
	}
}
