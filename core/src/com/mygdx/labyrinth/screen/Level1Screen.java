package com.mygdx.labyrinth.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.labyrinth.Constante;
import com.mygdx.labyrinth.Resource;
import com.mygdx.labyrinth.engine.Engine;
import com.mygdx.labyrinth.entity.Entity;
import com.mygdx.labyrinth.entity.EntityFactory;
import com.mygdx.labyrinth.entity.component.*;
import com.mygdx.labyrinth.input.Cursor;
import com.mygdx.labyrinth.input.GamePadAction;

import java.util.*;

public final class Level1Screen implements Screen {

    private final Engine engine;

    private boolean drawHitboxes;

    private TiledMap map;
    private OrthogonalTiledMapRenderer tileMap;

    private final ShapeRenderer debbug;

    private long timerTire;

    public Level1Screen(Engine engine) {
        this.engine = engine;
        this.drawHitboxes = false;
        this.debbug = new ShapeRenderer();
        this.timerTire = System.currentTimeMillis();
    }

    @Override
    public void show() {
        this.map = new TmxMapLoader().load(Resource.MAP_LEVEL_1);
        this.tileMap = new OrthogonalTiledMapRenderer(map, 1 / 16f);
        float mapWidth = (float) map.getProperties().get("width", int.class);
        float mapHeight = (float) map.getProperties().get("height", int.class);

        // COIN
        Random rng = new Random();
        for (int i = 0; i < 100; i++) {
            float rngX = rng.nextFloat() * (50 - 2) + 1;
            float rngY = rng.nextFloat() * (50 - 3) + 1;
            Entity coin = EntityFactory.createCoin("coin-" + i, rngX, rngY);
            this.engine.getEntityManager().add(coin);
        }

        // HERO
        MapObject spawnHero = this.tileMap.getMap().getLayers().get("SpawnHero").getObjects().get("spawn_hero");
        float x = spawnHero.getProperties().get("x", float.class) / 16;
        float y = spawnHero.getProperties().get("y", float.class) / 16;
        Entity hero = EntityFactory.createHero(x, y);
        this.engine.getEntityManager().add(hero);

        // WEAPON
        Entity bow = EntityFactory.createBow("bow", x + 0.2f, y, 0.3f, 1);
        this.engine.getEntityManager().add(bow);

        // MONSTERS
        // MASKULL
        for (int i = 0 ; i < 5 ; i++) {
            Entity maskull = EntityFactory.createMaskull("maskull" + i, x + 3*i, y + 3*i);
            this.engine.getEntityManager().add(maskull);
        }

        // CAMERA
        Entity camera = EntityFactory.createCamera();
        MusicLevel musicLevel = camera.getComponent(MusicLevel.class);
        musicLevel.init();
        FollowingCamera followingCamera = camera.getComponent(FollowingCamera.class);
        followingCamera.init();
        this.engine.getEntityManager().add(camera);

        // WALLS
        Entity upWall = EntityFactory.createWall("topWall", 0, mapHeight - 1f, mapWidth, 1f);
        Entity downWall = EntityFactory.createWall("botWall", 0, 0, mapWidth, 1.25f);
        Entity leftWall = EntityFactory.createWall("leftWall", 0, 0, 0.25f, mapHeight);
        Entity rightWall = EntityFactory.createWall("rightWall", mapWidth - 0.25f, 0, 1f, mapHeight);
        this.engine.getEntityManager().add(upWall);
        this.engine.getEntityManager().add(downWall);
        this.engine.getEntityManager().add(leftWall);
        this.engine.getEntityManager().add(rightWall);

        //HUD
        Vector2 posHud = new Vector2(this.engine.getCamera().position.x - this.engine.getCamera().viewportWidth / 2f,
                this.engine.getCamera().position.y + this.engine.getCamera().viewportHeight / 2f - 2f);
        Entity hudLife = EntityFactory.createLifeHud("hudLife", posHud, 1.5f, 1.5f);
        Entity hudArgent = EntityFactory.createHudArgent("hudArgent", posHud, 1.3f, 1.3f);
        this.engine.getEntityManager().add(hudLife);
        this.engine.getEntityManager().add(hudArgent);


        this.engine.getEntityManager().sortBodies();
    }

    @Override
    public void render(float delta) {

        // UPDATE
        this.updateCamera();
        this.updateCoins(delta);
        this.updateHero(delta);
        this.updateMaskull(delta);
        this.updateBow(delta);
        this.updateArrow(delta);
        this.updateHudLife(delta);
        this.updateHudArgent();

        this.checkCollision();

        // RENDER
        ScreenUtils.clear(0, 0, 0, 1);
        this.engine.getBatch().setProjectionMatrix(this.engine.getCamera().combined);
        this.tileMap.setView(this.engine.getCamera());
        this.tileMap.render();
        this.engine.getBatch().begin();

        this.renderCoin();
        this.renderHero();
        this.renderMaskull();
        this.renderBow();
        this.renderArrow();
        this.renderHudLife();
        this.renderHudArgent();

        this.engine.getBatch().end();
        if (this.engine.getInputManager().isPressed(GamePadAction.DRAW_HITBOX)) {
            this.drawHitboxes = !this.drawHitboxes;
        }
        if (this.drawHitboxes) {
            this.drawHitBox(this.engine.getCamera());
        }
    }

    @Override
    public void resize(int width, int height) {
        this.engine
                .getEntityManager()
                .findByNameAndComponent("camera", FollowingCamera.class)
                .getViewport()
                .update(width, height);
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
        Resource.dispose();
    }

    private void drawHitBox(OrthographicCamera camera) {
        this.debbug.setProjectionMatrix(camera.combined);
        List<HitBox> hitBoxes = this.engine.getEntityManager().findOneByComponent(HitBox.class);
        List<Vision> visions = this.engine.getEntityManager().findOneByComponent(Vision.class);
        this.debbug.begin(ShapeRenderer.ShapeType.Line);
        this.debbug.setColor(Color.RED);
        for (HitBox h : hitBoxes) {
            this.debbug.polygon(h.getBox().getTransformedVertices());
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
        List<Entity> coins = this.engine.getEntityManager().findByGroupName("coins");
        for (Entity coin : coins) {
            AnimatedSprite animatedSprite = coin.getComponent(AnimatedSprite.class);
            animatedSprite.update(deltaTime);
        }
    }

    private void renderCoin() {
        List<Entity> coins = this.engine.getEntityManager().findByGroupName("coins");
        for (Entity coin : coins) {
            HitBox hitBox = coin.getComponent(HitBox.class);
            AnimatedSprite animatedSprite = coin.getComponent(AnimatedSprite.class);
            TextureRegion sprite = animatedSprite.getFrame(true);
            this.engine.getBatch().draw(
                    sprite,
                    hitBox.getX(),
                    hitBox.getY(),
                    sprite.getRegionWidth() / 16f,
                    sprite.getRegionHeight() / 16f
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
        SoundPlayer walkSound = hero.getComponent(SoundPlayer.class);

        walkSound.cooldown();
        if(walkSound.operational() && (this.engine.getInputManager().isPressed(GamePadAction.RIGHT) ||
                this.engine.getInputManager().isPressed(GamePadAction.LEFT) ||
                this.engine.getInputManager().isPressed(GamePadAction.UP)   ||
                this.engine.getInputManager().isPressed(GamePadAction.DOWN) ||
                this.engine.getInputManager().isPressed(GamePadAction.W) ||
                this.engine.getInputManager().isPressed(GamePadAction.A) ||
                this.engine.getInputManager().isPressed(GamePadAction.S)   ||
                this.engine.getInputManager().isPressed(GamePadAction.D))) {
            walkSound.restartCooldown();
            long id = walkSound.getSound().play(0.3f);
            walkSound.getSound().setPitch(id, 2);
            walkSound.getSound().setLooping(id, false);
        }

        hitBox.getOldPosition().set(hitBox.getX(), hitBox.getY());

        if (this.engine.getInputManager().isPressed(GamePadAction.UP)
                || this.engine.getInputManager().isPressed(GamePadAction.W)) {
            direction.getValue().y = 1;
        } else if (this.engine.getInputManager().isPressed(GamePadAction.DOWN)
                || this.engine.getInputManager().isPressed(GamePadAction.S)) {
            direction.getValue().y = -1;
        } else {
            direction.getValue().y = 0;
        }

        if (this.engine.getInputManager().isPressed(GamePadAction.RIGHT)
                || this.engine.getInputManager().isPressed(GamePadAction.D)) {
            direction.getValue().x = 1;
        } else if (this.engine.getInputManager().isPressed(GamePadAction.LEFT)
                || this.engine.getInputManager().isPressed(GamePadAction.A)) {
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

        hitBox.setX(hitBox.getX()+ direction.getValue().x * velocity.getValue());
        hitBox.setY(hitBox.getY()+direction.getValue().y * velocity.getValue());

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
                hitBox.getX(),
                hitBox.getY(),
                sprite.getRegionWidth() / 16f,
                sprite.getRegionHeight() / 16f
        );
    }

    private void updateBow(float deltaTime) {
        Entity hero = this.engine.getEntityManager().findByName("hero");
        Direction heroDirection = hero.getComponent(Direction.class);
        HitBox heroHitBox = hero.getComponent(HitBox.class);

        Entity bow = this.engine.getEntityManager().findByName("bow");
        AnimatedSpriteList animatedSpriteList = bow.getComponent(AnimatedSpriteList.class);
        HitBox hitBox = bow.getComponent(HitBox.class);

        if (heroDirection.getValue().x > 0) {
            hitBox.setX(heroHitBox.getX()+ 1f);
        }

        if (heroDirection.getValue().x < 0) {
            hitBox.setX(heroHitBox.getX() - 0.3f);
        }

        hitBox.setY(heroHitBox.getY());
        animatedSpriteList.update(deltaTime);
    }

    private void renderBow() {
        Entity hero = this.engine.getEntityManager().findByName("hero");
        AnimatedSpriteList heroAnimatedSpriteList = hero.getComponent(AnimatedSpriteList.class);

        Entity bow = this.engine.getEntityManager().findByName("bow");
        AnimatedSpriteList animatedSpriteList = bow.getComponent(AnimatedSpriteList.class);
        HitBox hitBox = bow.getComponent(HitBox.class);

        TextureRegion sprite = animatedSpriteList.getFrame(true);

        if (heroAnimatedSpriteList.isFlipX() && !sprite.isFlipX()) {
            sprite.flip(true, false);
        }
        if (!heroAnimatedSpriteList.isFlipX() && sprite.isFlipX()) {
            sprite.flip(true, false);
        }

        this.engine.getBatch().draw(
                sprite,
                hitBox.getX(),
                hitBox.getY(),
                sprite.getRegionWidth() / 16f,
                sprite.getRegionHeight() / 16f
        );

    }

    private void updateArrow(float deltaTime) {
        Entity bow = this.engine.getEntityManager().findByName("bow");

        Cursor cursor = this.engine.getInputManager().getCursor();

        long currentTime = System.currentTimeMillis();
        if (cursor.isPressed() && currentTime - timerTire > Constante.TEMPS_ENTRE_DEUX_TIRE) {

            // Attention renvoie l'origine en haut à gauche !!!
            HitBox bowHitBox = bow.getComponent(HitBox.class);

            // Récupération des coordonnées du clic (en pixel : origine en haut à gauche)
            Vector3 mouseTmp = new Vector3(cursor.getPosition().x, cursor.getPosition().y, 0f);

            // Pour récupérer les coordonnées en fonction du monde
            this.engine.getCamera().unproject(mouseTmp);
            Vector2 mouse = new Vector2(mouseTmp.x, mouseTmp.y);


            Vector2 centerBow = new Vector2(bowHitBox.getX() - bowHitBox.getWidth()/ 2f,
                    bowHitBox.getY() + bowHitBox.getHeight() / 2f);


            Vector2 direction = mouse.sub(centerBow);

            Entity arrow = EntityFactory.createArrow(
                    "arrow-" + UUID.randomUUID(),
                    "heroArrows",
                    bowHitBox.getX(),
                    bowHitBox.getY(),
                    0.5f,
                    1f,
                    direction.nor().scl(Constante.VELOCITY_ARROW)
            );
            this.engine.getEntityManager().add(arrow);
            timerTire = currentTime;
        }

        List<Entity> arrows = this.engine.getEntityManager().findByGroupName("heroArrows");
        for (Entity arrow: arrows) {
            HitBox hitBox = arrow.getComponent(HitBox.class);
            Trajectory trajectory = arrow.getComponent(Trajectory.class);
            Vector2 v = new Vector2(hitBox.getX(), hitBox.getY());
            v.add(trajectory.getVector());
            hitBox.setPosition(v.x, v.y);
        }
    }

    private void renderArrow() {
        List<Entity> arrows = this.engine.getEntityManager().findByGroupName("heroArrows");

        for (Entity arrow : arrows) {
            HitBox hitBox = arrow.getComponent(HitBox.class);
            StaticSprite sprite = arrow.getComponent(StaticSprite.class);
            TextureRegion texture = sprite.getTextureRegion();
            Trajectory trajectory = arrow.getComponent(Trajectory.class);
            float x = hitBox.getX();
            float y = hitBox.getY();
            float w = texture.getRegionWidth() / 16f;
            float h = texture.getRegionHeight() / 16f;
            float ox = hitBox.getBox().getOriginX();
            float oy = hitBox.getBox().getOriginY();
            float sx = 1f;
            float sy = 1f;
            float r = trajectory.getAngle() - 90f;

            this.engine.getBatch().draw(
                    texture,
                    x,
                    y,
                    ox,
                    oy,
                    w,
                    h,
                    sx,
                    sy,
                    r
            );
        }
    }

    private void updateMaskull(float deltaTime) {
        List<Entity> enemies = new ArrayList<>();
        for (Entity e : this.engine.getEntityManager().getEntities()) {
            if (e.getName().contains("maskull")) {
                enemies.add(e);
            }
        }

        Entity hero = this.engine.getEntityByName("hero");
        for (Entity enemy : enemies) {
            //enemy = this.engine.getEntityByName("maskull" + cpt);
            AnimatedSpriteList animatedSpriteList = enemy.getComponent(AnimatedSpriteList.class);
            animatedSpriteList.update(deltaTime);

            Direction direction = enemy.getComponent(Direction.class);
            HitBox hitBox = enemy.getComponent(HitBox.class);
            HitBox heroHitBox = hero.getComponent(HitBox.class);
            Velocity velocity = enemy.getComponent(Velocity.class);
            Vision vision = enemy.getComponent(Vision.class);
            Vie vie = enemy.getComponent(Vie.class);

            if (vie.getVie() == 0) {
                enemy.addComponent(CollisionStatus.MARK_AS_REMOVE);
            }

            hitBox.getOldPosition().set(hitBox.getX(), hitBox.getY());

            //TODO à refaire collision cercle-enemy
            if (Intersector.overlaps(vision.getValue(), heroHitBox.getBox().getBoundingRectangle())) {

                Vector2 posHero = new Vector2(heroHitBox.getX(), heroHitBox.getY());
                Vector2 posMaskull = new Vector2(hitBox.getX(), hitBox.getY());

                direction.getValue().set(posHero.sub(posMaskull));

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

            hitBox.setX(hitBox.getX() + direction.getValue().x * velocity.getValue());
            vision.getValue().x += direction.getValue().x * velocity.getValue();
            hitBox.setY(hitBox.getY() + direction.getValue().y * velocity.getValue());
            vision.getValue().y += direction.getValue().y * velocity.getValue();

            if (direction.getValue().x > 0) {
                animatedSpriteList.setFlipX(false);
            }
            if (direction.getValue().x < 0) {
                animatedSpriteList.setFlipX(true);
            }

        }
    }

    private void renderMaskull() {
        List<Entity> enemies = new ArrayList<>();
        for (Entity e : this.engine.getEntityManager().getEntities()) {
            if (e.getName().contains("maskull")) {
                enemies.add(e);
            }
        }
        for (Entity enemy : enemies) {

            //enemy = this.engine.getEntityManager().findByName("maskull" + cpt);
            AnimatedSpriteList animatedSpriteList = enemy.getComponent(AnimatedSpriteList.class);
            TextureRegion sprite = animatedSpriteList.getFrame(true);
            if (animatedSpriteList.isFlipX() && !sprite.isFlipX()) {
                sprite.flip(true, false);
            }

            if (!animatedSpriteList.isFlipX() && sprite.isFlipX()) {
                sprite.flip(true, false);
            }

            HitBox hitBox = enemy.getComponent(HitBox.class);
            this.engine.getBatch().draw(
                    sprite,
                    hitBox.getX(),
                    hitBox.getY(),
                    sprite.getRegionWidth() / 16f,
                    sprite.getRegionHeight() / 16f
            );

        }
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
                hitBox.getX(),
                hitBox.getY(),
                mapWidth,
                mapHeight
        );

        followingCamera.getCamera().update();
        this.engine.getBatch().setProjectionMatrix(followingCamera.getCamera().combined);
        this.tileMap.setView(followingCamera.getCamera());
    }

    private void checkCollision() {
        this.engine.getEntityManager().getEntities()
                .removeIf(e -> e.hasComponent(CollisionStatus.class)
                        && e.getComponent(CollisionStatus.class).equals(CollisionStatus.MARK_AS_REMOVE));

        this.engine.getEntityManager().sortBodies();

        ArrayList<Entity> dynamicBodies = new ArrayList<>(this.engine.getEntityManager().getDynamicBodies());
        ArrayList<Entity> staticBodies = new ArrayList<>(this.engine.getEntityManager().getStaticBodies());

        for (int i = 0; i < dynamicBodies.size(); i++) {
            for (int j = 0; j < dynamicBodies.size(); j++) {
                HitBox hb1 = dynamicBodies.get(i).getComponent(HitBox.class);
                HitBox hb2 = dynamicBodies.get(j).getComponent(HitBox.class);

                if (hb1.isActive() && hb2.isActive()) {
                    if (Intersector.overlapConvexPolygons(hb1.getBox(), hb2.getBox())) {
                        dynamicBodies.get(i).handleCollision(dynamicBodies.get(j));
                    }
                }
            }
        }

        // Check collisions entre des entités dynamiques
        for (Entity dynamicBody : dynamicBodies) {
            for (Entity staticBody : staticBodies) {
                HitBox hb1 = dynamicBody.getComponent(HitBox.class);
                HitBox hb2 = staticBody.getComponent(HitBox.class);
                if (hb1 != null && hb2 != null) {
                    if (hb1.isActive() && hb2.isActive()) {
                        if (Intersector.overlapConvexPolygons(hb1.getBox(), hb2.getBox())) {
                            dynamicBody.handleCollision(staticBody);
                        }
                    }
                }
            }
        }

        this.engine.getEntityManager().clearBodies();
    }

    private void updateHudLife(float delta) {
        Entity hudLife = this.engine.getEntityManager().findByName("hudLife");
        Position posHudLife = hudLife.getComponent(Position.class);
        posHudLife.getValue().set(this.engine.getCamera().position.x - this.engine.getCamera().viewportWidth / 2f + 0.3f,
                this.engine.getCamera().position.y + this.engine.getCamera().viewportHeight / 2f - 1.6f);
    }

    private void renderHudLife() {
        Entity hudLife = this.engine.getEntityManager().findByName("hudLife");
        HudLife hud = hudLife.getComponent(HudLife.class);
        Position posHud = hudLife.getComponent(Position.class);
        Vie vieHero = this.engine.getEntityManager().findByName("hero").getComponent(Vie.class);

        for (int i = 0; i < vieHero.getVie() / 2; i++) {
            this.engine.getBatch().draw(hud.getFullHeart().getTexture(),
                    posHud.getValue().x + (float) i * 1.5f,
                    posHud.getValue().y,
                    hud.getWidth(),
                    hud.getHeight());
        }
    }

    private void updateHudArgent() {
        Entity hudLife = this.engine.getEntityManager().findByName("hudArgent");
        Position posHudArgent = hudLife.getComponent(Position.class);
        posHudArgent.getValue().set(this.engine.getCamera().position.x - this.engine.getCamera().viewportWidth / 2f + 7f,
                this.engine.getCamera().position.y + this.engine.getCamera().viewportHeight / 2f - 1.4f);
    }

    private void renderHudArgent() {
        Entity hudArgent = this.engine.getEntityManager().findByName("hudArgent");
        Entity hero = this.engine.getEntityManager().findByName("hero");
        Position posHud = hudArgent.getComponent(Position.class);
        Dimension dimensionHud = hudArgent.getComponent(Dimension.class);
        StaticSprite sprite = hudArgent.getComponent(StaticSprite.class);
        Font fontHud = hudArgent.getComponent(Font.class);
        Argent argentHero = hero.getComponent(Argent.class);

        this.engine.getBatch().draw(sprite.getTexture(),
                posHud.getValue().x,
                posHud.getValue().y,
                dimensionHud.getWidth(),
                dimensionHud.getHeight());

        fontHud.getFont().draw(this.engine.getBatch(),
                argentHero.getArgent() + "",
                posHud.getValue().x + 1.7f,
                posHud.getValue().y + 1f);
    }


}
