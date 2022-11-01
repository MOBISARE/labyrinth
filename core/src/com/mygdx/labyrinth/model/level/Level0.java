package com.mygdx.labyrinth.model.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
import com.mygdx.labyrinth.model.*;
import com.mygdx.labyrinth.model.Observable;

import java.util.*;
import java.util.stream.Collectors;

public final class Level0 extends Observable implements Screen{

    //region Attributs

    /**
     * Classe principal qui représente le jeu
     */
    private final Labyrinth rootGame;


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

        MapObject spawnHero = renderer.getMap().getLayers().get("SpawnHero").getObjects().get("spawn_hero");
        Hero hero = new Hero(
                spawnHero.getProperties().get("x", float.class) / 16f,
                spawnHero.getProperties().get("y", float.class) / 16f,
                0.8f,
                1f);
        Enemy enemy = new Enemy("Enemy_0",
                spawnHero.getProperties().get("x", float.class) / 16f,
                spawnHero.getProperties().get("y", float.class) / 16f,
                0.8f,
                1f);



        // Creation de pièces
        createRandomCoin(100);

        EntityManager.getInstance().add(hero);
        EntityManager.getInstance().add(enemy);
    }


    /**
     * {@inheritDoc}
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {

        // UPDATE
        setPosCamera();
        EntityManager.getInstance().removeIf(Entity::isDestroyed);

        // RENDER
        ScreenUtils.clear(0, 0, 0, 1);

        // Dessine la partie que la caméra regarde
        this.renderer.setView(this.camera);

        // Dessine la carte
        this.renderer.render(new int[]{0});
        this.rootGame.getBatch().setProjectionMatrix(this.camera.combined);
        this.rootGame.getBatch().begin();

        EntityManager.getInstance().render(this.rootGame.getBatch(), delta);

        this.rootGame.getBatch().end();
        this.renderer.render(new int[]{1});
    }

    /**
     * Positionne la caméra en fonction du héro et des bords de la map
     */
    private void setPosCamera() {
        float x, y = 0;
        Hero hero = (Hero) EntityManager.getInstance().findByName("Hero").orElseThrow(() -> new IllegalArgumentException("Hero non trouvé"));

        if (hero.getPosition().x - this.camera.viewportWidth / 2f < 0) {
            x = this.camera.viewportWidth / 2f;
        } else if (hero.getPosition().x + this.camera.viewportWidth / 2f > this.mapWidth){
            x = this.mapWidth - this.camera.viewportWidth / 2f;
        } else {
            x = hero.getPosition().x;
        }

        if (hero.getPosition().y - this.camera.viewportHeight / 2f < 0) {
            y = this.camera.viewportHeight / 2f;
        } else if (hero.getPosition().y + this.camera.viewportHeight / 2f > this.mapHeight) {
            y = this.mapHeight - this.camera.viewportHeight / 2f;
        } else {
            y = hero.getPosition().y;
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
     * Créer un nombre aléatoire de pièces sur la map
     * @param nb nombre de pièces crée
     */
    private void createRandomCoin(int nb) {
        Random rand = new Random();

        for (int i = 0; i < nb; i++) {
            Coin c = new Coin((rand.nextFloat() * (mapWidth - 0.5f) + 0.25f),
                    rand.nextFloat() * (mapHeight - 3.25f) + 1.25f, 0.4f,0.4f);
            EntityManager.getInstance().add(c);
        }
    }

    /**
     * Libère les resources
     */
    public void dispose() {
        EntityManager.getInstance().dispose();
        this.music.dispose();
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
