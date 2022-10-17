package com.mygdx.labyrinth.game.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.labyrinth.controller.Observer;
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

    private int mapWidth;
    private int mapHeight;

    private List<Observer> observers;

    /**
     * Classe principal qui contient l'ensemble des éléments du jeux et qui permet
     * de gérer la physique
     */
    public Level0(Labyrinth rootGame) {
        this.rootGame = rootGame;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 20f, 18f);
        this.camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
        this.viewport = new ExtendViewport(18f, 12f, this.camera);
        this.viewport.apply();
        TiledMap map = new TmxMapLoader().load("levels/map_test.tmx");
        this.mapWidth = Integer.parseInt(map.getProperties().get("width").toString());
        this.mapHeight = Integer.parseInt(map.getProperties().get("height").toString());
        this.renderer = new OrthogonalTiledMapRenderer(map, 1/16f);

        this.entities = new ArrayList<>();
        this.hero = new Hero(3f,3f,1f,1f, (TiledMapTileLayer) map.getLayers().get(0));
        InputProcessorHero inputProcessorHero = new InputProcessorHero(hero);
        Gdx.input.setInputProcessor(inputProcessorHero);

        this.entities.add(this.hero);
        this.observers = new ArrayList<>();
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        // UPDATE
        setPosCamera();

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

    private void setPosCamera() {
        float x = 0;
        float y = 0;

        if (this.hero.getPosition().x - this.camera.viewportWidth / 2f < 0) {
            x = this.camera.viewportWidth / 2f;
        } else if (this.hero.getPosition().x + this.camera.viewportWidth / 2f > this.mapWidth){
            x = this.mapWidth - this.camera.viewportWidth / 2f;
        } else {
            x = this.hero.getPosition().x;
        }

        if (this.hero.getPosition().y - this.camera.viewportHeight / 2f < 0) {
            y = this.camera.viewportHeight / 2f;
        } else if (this.hero.getPosition().y + this.camera.viewportHeight / 2f > this.mapHeight) {
            y = this.mapHeight - this.camera.viewportHeight / 2f;
        } else {
            y = this.hero.getPosition().y;
        }

        this.camera.position.set(x, y, 0);
        this.camera.update();
        this.updateObservers();
    }

    public void addObserver(Observer o) {
        this.observers.add(o);
    }

    private void updateObservers() {
        this.observers.forEach(Observer::update);
    }

    public OrthographicCamera getCamera() {
        return this.camera;
    }
}
