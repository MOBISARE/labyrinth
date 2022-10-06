package com.mygdx.labyrinth.worldl;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface WorldObject {

    public void update(float deltaTime);
    public void draw(SpriteBatch batch, float deltaTime);
    public void dispose();
}
