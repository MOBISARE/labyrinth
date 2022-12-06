package com.mygdx.labyrinth.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.mygdx.labyrinth.Resource;
import com.mygdx.labyrinth.engine.Engine;
import com.mygdx.labyrinth.input.GamePadAction;

public class EndScreen implements Screen {

    private final Engine engine;

    public EndScreen(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        this.engine.getBatch().begin();
        this.engine.getBatch().draw(Resource.BACKGROUND_ENDMENU, 1, 1);
        this.engine.getBatch().end();

        if (this.engine.getInputManager().isPressed(GamePadAction.QUIT)) {
            this.dispose();
            Resource.dispose();
            Gdx.app.exit();
        }
        if(this.engine.getInputManager().isPressed(GamePadAction.PLAY)) {
            this.restart();
        }
    }

    public void restart() {
        this.engine.setScreen(new Level1Screen(engine));
        this.dispose();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}
}
