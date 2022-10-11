package com.mygdx.labyrinth.game.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.labyrinth.game.Labyrinth;
import com.mygdx.labyrinth.model.InputProcessorHero;
import com.mygdx.labyrinth.model.Hero;
import com.mygdx.labyrinth.model.Entity;

import java.util.ArrayList;
import java.util.List;

public final class Level0 implements Screen {

    private final Labyrinth rootGame;
    private final Hero hero;

    private final OrthographicCamera camera;

    private Viewport viewport;

    private final OrthogonalTiledMapRenderer renderer;

    /**
     * Contient la liste des éléments composants le monde
     */
    private final List<Entity> entities;

    /**
     * Classe principal qui contient l'ensemble des éléments du jeux et qui permet
     * de gérer la physique
     */
    public Level0(Labyrinth rootGame) {
        this.rootGame = rootGame;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 32f, 18f);
        this.camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
        this.viewport = new StretchViewport(32f, 18f, this.camera);
        this.viewport.apply();
        TiledMap map = new TmxMapLoader().load("levels/map_test.tmx");
        this.renderer = new OrthogonalTiledMapRenderer(map, 1/16f);

        this.entities = new ArrayList<>();
        this.hero = new Hero(2f,2f,1f,1.5f, (TiledMapTileLayer) map.getLayers().get(0));
        InputProcessorHero inputProcessorHero = new InputProcessorHero(hero);
        Gdx.input.setInputProcessor(inputProcessorHero);

        this.entities.add(this.hero);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        // UPDATE
        this.camera.position.set(this.hero.getPosition().x, this.hero.getPosition().y, 0);
        this.camera.update();

        // RENDER
        ScreenUtils.clear(0, 0, 0, 1);

        // Dessine la partie que la caméra regarde
        this.renderer.setView(this.camera);

        // Dessine la carte
        this.renderer.render();
        this.rootGame.getBatch().setProjectionMatrix(this.camera.combined);
        this.rootGame.getBatch().begin();

        this.entities.forEach(e -> e.render(this.rootGame.getBatch(), delta));

        this.rootGame.getBatch().end();

    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);

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

    public void dispose() {
        this.entities.forEach(Entity::dispose);
    }

    public Hero getHero() {
        return hero;
    }

    public List<Entity> getEntities() {
        return entities;
    }
}
