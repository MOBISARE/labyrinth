package fr.univlorraine.etu.labyrinth.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.utils.ScreenUtils;
import fr.univlorraine.etu.labyrinth.Resource;
import fr.univlorraine.etu.labyrinth.engine.Engine;
import fr.univlorraine.etu.labyrinth.entity.Entity;
import fr.univlorraine.etu.labyrinth.entity.EntityFactory;
import fr.univlorraine.etu.labyrinth.entity.component.*;
import fr.univlorraine.etu.labyrinth.input.GamePadAction;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public final class Level1Screen implements Screen {

    private final Engine engine;

    private TiledMap map;
    private OrthogonalTiledMapRenderer tileMap;

    private final ShapeRenderer debbug;

    public Level1Screen(Engine engine) {
        this.engine = engine;
        this.debbug = new ShapeRenderer();
    }

    @Override
    public void show() {
        this.map = new TmxMapLoader().load(Resource.MAP_LEVEL_1);
        this.tileMap = new OrthogonalTiledMapRenderer(map, Engine.SCALE);

        // COIN
        Random rng = new Random();
        for (int i = 0; i < 100; i++) {
            int x = map.getProperties().get("width", int.class) * Engine.TILE_SIZE * Engine.SCALE;
            int y = map.getProperties().get("height", int.class) * Engine.TILE_SIZE * Engine.SCALE;
            float rngX = MathUtils.clamp(rng.nextInt(x), Engine.TILE_SIZE * Engine.SCALE, x - Engine.TILE_SIZE * Engine.SCALE);
            float rngY = MathUtils.clamp(rng.nextInt(y), Engine.TILE_SIZE * Engine.SCALE, y - Engine.TILE_SIZE * Engine.SCALE);
            Entity coin = EntityFactory.createCoin("coin-" + i, rngX, rngY);
            this.engine.getEntityManager().add(coin);
        }

        // HERO
        MapObject spawnHero = this.tileMap.getMap().getLayers().get("SpawnHero").getObjects().get("spawn_hero");
        float x = spawnHero.getProperties().get("x", float.class) / Engine.TILE_SIZE * Engine.SCALE;
        float y = spawnHero.getProperties().get("y", float.class) / Engine.TILE_SIZE * Engine.SCALE;
        Entity hero = EntityFactory.createHero(x, y);
        this.engine.getEntityManager().add(hero);

        // MONSTERS
        // MASKULL
        Entity maskull = EntityFactory.createMaskull("maskull", x + 10, y + 10);
        this.engine.getEntityManager().add(maskull);

        // CAMERA
        Entity camera = EntityFactory.createCamera();
        FollowingCamera followingCamera = camera.getComponent(FollowingCamera.class);
        followingCamera.init();
        this.engine.getEntityManager().add(camera);

    }

    @Override
    public void render(float delta) {
        // UPDATE
        this.updateCamera();
        this.updateCoins(delta);
        this.updateHero(delta);
        this.updateMaskull(delta);
        this.checkCollision();

        // RENDER
        ScreenUtils.clear(0, 0, 0, 1);
        this.tileMap.render();
        this.engine.getBatch().begin();

        this.renderCoin();
        this.renderHero();
        this.renderMaskull();

        this.engine.getBatch().end();
        this.drawHitBox(this.engine
                .getEntityManager()
                .findByNameAndComponent("camera", FollowingCamera.class)
                .getCamera());
    }

    @Override
    public void resize(int width, int height) {
        this.engine
                .getEntityManager()
                .findByNameAndComponent("camera", FollowingCamera.class)
                .resize(width, height);
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
    public void dispose() {
        this.tileMap.dispose();
    }

    private void drawHitBox(OrthographicCamera camera) {
        this.debbug.setProjectionMatrix(camera.combined);
        List<HitBox> hitBoxes = this.engine.getEntityManager().findByComponent(HitBox.class);
        List<Vision> visions = this.engine.getEntityManager().findByComponent(Vision.class);
        this.debbug.begin(ShapeRenderer.ShapeType.Line);
        this.debbug.setColor(Color.RED);
        for (HitBox h : hitBoxes) {
            this.debbug.rect(
                    h.getValue().x,
                    h.getValue().y,
                    h.getValue().width,
                    h.getValue().height);
        }
        for (Vision v : visions) {
            this.debbug.circle(
                    v.getValue().x,
                    v.getValue().y,
                    v.getValue().radius
            );
        }
        this.debbug.end();
    }

    private void updateCoins(float deltaTime) {
        List<Entity> coins = this.engine.getEntityManager().findByGroupName("coin");
        for (Entity coin : coins) {
            AnimatedSprite animatedSprite = coin.getComponent(AnimatedSprite.class);
            animatedSprite.update(deltaTime);
        }
    }

    private void renderCoin() {
        List<Entity> coins = this.engine.getEntityManager().findByGroupName("coin");
        for (Entity coin : coins) {
            HitBox hitBox = coin.getComponent(HitBox.class);
            AnimatedSprite animatedSprite = coin.getComponent(AnimatedSprite.class);
            TextureRegion sprite = animatedSprite.getFrame(true);
            this.engine.getBatch().draw(
                    sprite,
                    hitBox.getValue().x,
                    hitBox.getValue().y,
                    sprite.getRegionWidth() * Engine.SCALE,
                    sprite.getRegionHeight() * Engine.SCALE
            );
        }
    }

    private void updateHero(float deltaTime) {
        Entity hero = this.engine.getEntityManager().findByName("hero");
        AnimatedSpriteList animatedSpriteList = hero.getComponent(AnimatedSpriteList.class);
        animatedSpriteList.update(deltaTime);

        Direction direction = hero.getComponent(Direction.class);
        HitBox hitBox = hero.getComponent(HitBox.class);
        Velocity velocity = hero.getComponent(Velocity.class);

        if (this.engine.getInputManager().isPressed(GamePadAction.UP)) {
            direction.getValue().y = 1;
        } else if (this.engine.getInputManager().isPressed(GamePadAction.DOWN)) {
            direction.getValue().y = -1;
        } else {
            direction.getValue().y = 0;
        }

        if (this.engine.getInputManager().isPressed(GamePadAction.RIGHT)) {
            direction.getValue().x = 1;
        } else if (this.engine.getInputManager().isPressed(GamePadAction.LEFT)) {
            direction.getValue().x = -1;
        } else {
            direction.getValue().x = 0;
        }

        if (direction.getValue().x != 0 && direction.getValue().y != 0) {
            direction.getValue().nor();
        }

        if (direction.getValue().x == 0 && direction.getValue().y == 0) {
            animatedSpriteList.setCurrentAnimationName("idle");
        } else {
            animatedSpriteList.setCurrentAnimationName("run");
        }

        hitBox.getValue().x += direction.getValue().x * velocity.getValue();
        hitBox.getValue().y += direction.getValue().y * velocity.getValue();

        if (direction.getValue().x > 0) {
            animatedSpriteList.setFlipX(false);
        }
        if (direction.getValue().x < 0) {
            animatedSpriteList.setFlipX(true);
        }
    }

    private void renderHero() {
        Entity hero = this.engine.getEntityManager().findByName("hero");
        AnimatedSpriteList animatedSpriteList = hero.getComponent(AnimatedSpriteList.class);
        TextureRegion sprite = animatedSpriteList.getFrame(true);

        if (animatedSpriteList.isFlipX() && !sprite.isFlipX()) {
            sprite.flip(true, false);
        }
        if (!animatedSpriteList.isFlipX() && sprite.isFlipX()) {
            sprite.flip(true, false);
        }

        HitBox hitBox = hero.getComponent(HitBox.class);
        this.engine.getBatch().draw(
                sprite,
                hitBox.getValue().x,
                hitBox.getValue().y,
                sprite.getRegionWidth() * Engine.SCALE,
                sprite.getRegionHeight() * Engine.SCALE
        );
    }

    private void updateMaskull(float deltaTime) {
        Entity maskull = this.engine.getEntityManager().findByName("maskull");
        Entity hero = this.engine.getEntityManager().findByName("hero");
        AnimatedSpriteList animatedSpriteList = maskull.getComponent(AnimatedSpriteList.class);
        animatedSpriteList.update(deltaTime);

        Direction direction = maskull.getComponent(Direction.class);
        HitBox hitBox = maskull.getComponent(HitBox.class);
        HitBox heroHitBox = hero.getComponent(HitBox.class);
        Velocity velocity = maskull.getComponent(Velocity.class);

        if (Intersector.overlaps(maskull.getComponent(Vision.class).getValue(),
                hero.getComponent(HitBox.class).getValue())) {

            if (hitBox.getValue().x < heroHitBox.getValue().x) {
                direction.getValue().x = 1;

            } else {
                direction.getValue().x = -1;;
            }

            if (hitBox.getValue().y < heroHitBox.getValue().y) {
                direction.getValue().y = 1;
            } else {
                direction.getValue().y = -1;
            }
        } else {
            direction.getValue().x = 0;
            direction.getValue().y = 0;
        }

        if (direction.getValue().x != 0 && direction.getValue().y != 0) {
            direction.getValue().nor();
        }

        if (direction.getValue().x == 0 && direction.getValue().y == 0) {
            animatedSpriteList.setCurrentAnimationName("idle");
        } else {
            animatedSpriteList.setCurrentAnimationName("run");
        }

        hitBox.getValue().x += direction.getValue().x * velocity.getValue();
        hitBox.getValue().y += direction.getValue().y * velocity.getValue();

        if (direction.getValue().x > 0) {
            animatedSpriteList.setFlipX(false);
        }
        if (direction.getValue().x < 0) {
            animatedSpriteList.setFlipX(true);
        }
    }

    private void renderMaskull() {
        Entity maskull = this.engine.getEntityManager().findByName("maskull");
        AnimatedSpriteList animatedSpriteList = maskull.getComponent(AnimatedSpriteList.class);
        TextureRegion sprite = animatedSpriteList.getFrame(true);
        if (animatedSpriteList.isFlipX() && !sprite.isFlipX()) {
            sprite.flip(true, false);
        }

        if (!animatedSpriteList.isFlipX() && sprite.isFlipX()) {
            sprite.flip(true, false);
        }

        HitBox hitBox = maskull.getComponent(HitBox.class);
        this.engine.getBatch().draw(
                sprite,
                hitBox.getValue().x,
                hitBox.getValue().y,
                sprite.getRegionWidth() * Engine.SCALE,
                sprite.getRegionHeight() * Engine.SCALE
        );
    }

    private void updateCamera() {

        HitBox hitBox = this.engine.getEntityManager().findByNameAndComponent("hero", HitBox.class);
        int mapWidth = this.map.getProperties().get("width", int.class);
        int mapHeight = this.map.getProperties().get("height", int.class);
        FollowingCamera followingCamera = this.engine
                .getEntityManager()
                .findByName("camera")
                .getComponent(FollowingCamera.class);

        followingCamera.follow(
                hitBox.getValue().x,
                hitBox.getValue().y,
                mapWidth * Engine.TILE_SIZE * Engine.SCALE,
                mapHeight * Engine.TILE_SIZE * Engine.SCALE
        );

        followingCamera.getCamera().update();
        this.engine.getBatch().setProjectionMatrix(followingCamera.getCamera().combined);
        this.tileMap.setView(followingCamera.getCamera());
    }

    private void checkCollision() {
        List<HitBox> hitboxes = this.engine.getEntityManager().findByComponent(HitBox.class);
        for (HitBox h0 : hitboxes) {
            for (HitBox h1 : hitboxes) {
                if (!Objects.equals(h0.getValue(), h1.getValue())) {
                    if (h0.getValue().overlaps(h1.getValue())) {
                        this.handleCollision(h0, h1);
                    }
                }
            }
        }
    }

    private void handleCollision(HitBox source, HitBox target) {

        Entity sourceEntity = this.engine.getEntityManager().findByComponent(source);
        Entity targetEntity = this.engine.getEntityManager().findByComponent(target);

        if(sourceEntity.hasComponent(Vision.class)) {
            Vision sourceVision = sourceEntity.getComponent(Vision.class);
            this.handleCollisionHeroToEnemyVision(sourceVision, target);
        }

        this.handleCollisionHeroToCoin(source, target);

        //System.out.println("Collision entre " + sourceEntity.getName() + " et " + targetEntity.getName());

    }

    private void handleCollisionHeroToCoin(HitBox source, HitBox target) {
        Entity sourceEntity = this.engine.getEntityManager().findByComponent(source);
        Entity targetEntity = this.engine.getEntityManager().findByComponent(target);
        if (Objects.equals(sourceEntity.getName(), "hero")
                && targetEntity.getName().startsWith("coin")) {
            this.engine.getEntityManager().removeByName(targetEntity.getName());
        }
    }

    private void handleCollisionHeroToEnemyVision(Vision source, HitBox target) {
        Entity sourceEntity = this.engine.getEntityManager().findByComponent(source);
        Entity targetEntity = this.engine.getEntityManager().findByComponent(target);

        if (sourceEntity.getName().startsWith("maskull")
                && Objects.equals(targetEntity.getName(), "hero")) {
            System.out.println(sourceEntity.getName() + " vous à repéré");


        }
    }
}
