package com.mygdx.labyrinth.model.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.labyrinth.exception.LabyrinthException;
import com.mygdx.labyrinth.game.Labyrinth;
import com.mygdx.labyrinth.controller.InputProcessorHero;
import com.mygdx.labyrinth.model.Hero;
import com.mygdx.labyrinth.model.Entity;
import com.mygdx.labyrinth.model.Observable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Level0 extends Observable implements Screen, Iterable<Entity> {

    //region Attributs

    /**
     * Classe principal qui représente le jeu
     */
    private final Labyrinth rootGame;

    /**
     * Hero de la partie
     */
    private final Hero hero;

    /**
     * Camera associé au niveau
     */
    private final OrthographicCamera camera;

    /**
     * Viewport associé à la caméra
     */
    private final Viewport viewport;

    /**
     * Moteur de rendu de la carte
     */
    private final OrthogonalTiledMapRenderer renderer;

    private Music music;

    /**
     * Contient la liste des éléments composants le monde
     */
    private final List<Entity> entities;

    /**
     * Largeur de la map
     */
    private final int mapWidth;

    /**
     * Hauteur de la map
     */
    private final int mapHeight;

    //endregion

    /**
     * Classe principal qui contient l'ensemble des éléments du jeux et qui permet
     * de gérer la physique
     */
    public Level0(Labyrinth rootGame) {
        super();
        this.rootGame = rootGame;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 20f, 18f);
        this.camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
        this.viewport = new ExtendViewport(18f, 12f, this.camera);
        this.viewport.apply();
        TiledMap map = new TmxMapLoader().load("levels/map_test2.tmx");
        this.mapWidth = Integer.parseInt(map.getProperties().get("width").toString());
        this.mapHeight = Integer.parseInt(map.getProperties().get("height").toString());
        this.renderer = new OrthogonalTiledMapRenderer(map, 1/16f);
        this.music = Gdx.audio.newMusic(Gdx.files.internal("sound/backgroundMusic.mp3"));
        music.setVolume(0.02f);
        music.setLooping(true);
        music.play();

        this.entities = new ArrayList<>();
        MapObject spawnHero = renderer.getMap().getLayers().get("SpawnHero").getObjects().get("spawn_hero");
        this.hero = new Hero(spawnHero.getProperties().get("x", float.class) / 16f
                ,spawnHero.getProperties().get("y", float.class) / 16f
                ,0.8f,1f);

        this.entities.add(this.hero);
    }


    /**
     * {@inheritDoc}
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {

        // UPDATE
        setPosCamera();

        // RENDER
        ScreenUtils.clear(0, 0, 0, 1);

        // Dessine la partie que la caméra regarde
        this.renderer.setView(this.camera);

        // Dessine la carte
        this.renderer.render(new int[]{0});
        this.rootGame.getBatch().setProjectionMatrix(this.camera.combined);
        this.rootGame.getBatch().begin();

        this.entities.forEach(e -> e.render(this.rootGame.getBatch(), delta));

        this.rootGame.getBatch().end();
        this.renderer.render(new int[]{1});
    }

    public List<Entity> getEntities() {
        return entities;
    }

    /**
     * Positionne la caméra en fonction du héro et des bords de la map
     */
    private void setPosCamera() {
        float x, y = 0;

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
        try {
            notifierObservers("camera_position_event", this.camera);
        } catch (LabyrinthException e) {
            throw new RuntimeException(e);
        }

        this.camera.update();
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public Iterator<Entity> iterator() {
        return entities.iterator();
    }

    /**
     * Retourne l'ensemble des objets de la map
     * @return MapObjects
     */
    public MapObjects getCollisionObjectOnMap() {
        return renderer.getMap().getLayers().get("Collisions").getObjects();
    }

    /**
     * Retourne la camera de l'ecran actuel
     * @return OrthographicCamera
     */
    public OrthographicCamera getCamera() {
        return camera;
    }

    /**
     * Retourne le héro
     * @return Hero
     */
    public Hero getHero() {
        return hero;
    }

    /**
     * Libère les resources
     */
    public void dispose() {
        this.entities.forEach(Entity::dispose);
        this.entities.clear();
        music.dispose();
    }

    /**
     * {@inheritDoc}
     * @param width
     * @param height
     */
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

    @Override
    public void show() {

    }
}
