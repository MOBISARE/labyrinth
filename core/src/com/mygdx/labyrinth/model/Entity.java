package com.mygdx.labyrinth.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Entity {

    void render(SpriteBatch batch, float deltaTime);
    void dispose();
}
