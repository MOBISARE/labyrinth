package com.mygdx.labyrinth.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
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
import com.mygdx.labyrinth.entity.component.camera.FollowingCamera;
import com.mygdx.labyrinth.entity.component.collisions.CollisionStatus;
import com.mygdx.labyrinth.entity.component.collisions.HitBox;
import com.mygdx.labyrinth.entity.component.features.Argent;
import com.mygdx.labyrinth.entity.component.features.Dimension;
import com.mygdx.labyrinth.entity.component.features.Vie;
import com.mygdx.labyrinth.entity.component.collisions.Vision;
import com.mygdx.labyrinth.entity.component.fonts.Font;
import com.mygdx.labyrinth.entity.component.hud.HudLife;
import com.mygdx.labyrinth.entity.component.movement.Direction;
import com.mygdx.labyrinth.entity.component.movement.Position;
import com.mygdx.labyrinth.entity.component.movement.Trajectory;
import com.mygdx.labyrinth.entity.component.movement.Velocity;
import com.mygdx.labyrinth.entity.component.sound.MusicLevel;
import com.mygdx.labyrinth.entity.component.sound.SoundPlayer;
import com.mygdx.labyrinth.entity.component.sprite.AnimatedSprite;
import com.mygdx.labyrinth.entity.component.sprite.AnimatedSpriteList;
import com.mygdx.labyrinth.entity.component.sprite.StaticSprite;
import com.mygdx.labyrinth.entity.component.timer.TimerManager;
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
//        Random rng = new Random();
//        for (int i = 0; i < 100; i++) {
//            float rngX = rng.nextFloat() * (50 - 2) + 1;
//            float rngY = rng.nextFloat() * (50 - 3) + 1;
//            Entity coin = EntityFactory.createCoin("coin-" + i, rngX, rngY);
//            this.engine.getEntityManager().add(coin);
//        }

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
        for (int i = 1 ; i < 3 ; i++) {
            String name = "spawn_mob" + i;
            Entity maskull = EntityFactory.createMaskull("maskull" + i, this.tileMap.getMap().getLayers().get("SpawnEnnemies").getObjects().get(name).getProperties().get("x", float.class)/16, this.tileMap.getMap().getLayers().get("SpawnEnnemies").getObjects().get(name).getProperties().get("y", float.class)/16);
            this.engine.getEntityManager().add(maskull);
        }

        // LITTLE
        for (int i = 0 ; i < 10 ; i++) {
            Entity little = EntityFactory.createLittle("little" + i, 2, 2);
            this.engine.getEntityManager().add(little);
        }

        // BIG ZOMBIE
        for (int i = 0 ; i < 3 ; i++) {
            Entity bigZombie = EntityFactory.createBigZombie("big_zombie" + i, 2, 2);
            this.engine.getEntityManager().add(bigZombie);
        }

        // BIG DEVIL
        for (int i = 0 ; i < 1 ; i++) {
            Entity bigDevil = EntityFactory.createBigDevil("big_devil" + i, 2, 2);
            this.engine.getEntityManager().add(bigDevil);
        }

        // MAGE
//        for (int i = 0 ; i < 1 ; i++) {
//            Entity mage = EntityFactory.createMage("mage" + i, x + 3*i, y + 3*i);
//            Entity staff = EntityFactory.createStaff("staff" + i,
//                    mage.getComponent(HitBox.class).getX() + 0.2f,
//                    mage.getComponent(HitBox.class).getY(),
//                    0.3f,
//                    1f);
//            this.engine.getEntityManager().add(mage);
//            this.engine.getEntityManager().add(staff);
//            this.timerEnemyAttack.add(0L);
//        }

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
        //Room1
        Entity room1downWall = EntityFactory.createWall("room1downWall", 4f, 13f, 9f, 1f);
        Entity room1upWall = EntityFactory.createWall("room1upWall", 4f, 21f, 9f, 1f);
        Entity room1leftWall = EntityFactory.createWall("room1leftWall", 4f, 13f, 0.33f, 9f);
        Entity room1rightWall = EntityFactory.createWall("room1rightWall", 12.66f, 18f, 0.33f, 4f);
        //Room2
        Entity room2downWall1 = EntityFactory.createWall("room2downWall1", 24f, 26f, 7f, 1f);
        Entity room2downWall2 = EntityFactory.createWall("room2downWall2", 33f, 26f, 10f, 1f);
        Entity room2upWall = EntityFactory.createWall("room2upWall", 24f, 38f, 19f, 1f);
        Entity room2leftWall = EntityFactory.createWall("room2leftWall", 24f, 26f, 0.33f, 13f);
        Entity room2rightWall = EntityFactory.createWall("room2rightWall", 42.66f, 26f, 0.33f, 13f);
        Entity marblePillar1 = EntityFactory.createWall("marblePillar1", 27f, 35f, 1f, 1.5f);
        Entity marblePillar2 = EntityFactory.createWall("marblePillar2", 27f, 29f, 1f, 1.5f);
        Entity marblePillar3 = EntityFactory.createWall("marblePillar3", 39f, 35f, 1f, 1.5f);
        Entity marblePillar4 = EntityFactory.createWall("marblePillar4", 39f, 29f, 1f, 1.5f);

        this.engine.getEntityManager().add(upWall);
        this.engine.getEntityManager().add(downWall);
        this.engine.getEntityManager().add(leftWall);
        this.engine.getEntityManager().add(rightWall);
        this.engine.getEntityManager().add(room1downWall);
        this.engine.getEntityManager().add(room1upWall);
        this.engine.getEntityManager().add(room1leftWall);
        this.engine.getEntityManager().add(room1rightWall);
        this.engine.getEntityManager().add(room2downWall2);
        this.engine.getEntityManager().add(room2upWall);
        this.engine.getEntityManager().add(room2leftWall);
        this.engine.getEntityManager().add(room2rightWall);
        this.engine.getEntityManager().add(room2downWall1);
        this.engine.getEntityManager().add(marblePillar1);
        this.engine.getEntityManager().add(marblePillar2);
        this.engine.getEntityManager().add(marblePillar3);
        this.engine.getEntityManager().add(marblePillar4);


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
        this.updateLittle(delta);
        this.updateBigZombie(delta);
        this.updateBigDevil(delta);
        //this.updateMage(delta);
        this.updateBow(delta);
        //this.updateStaff(delta);
        this.updateArrow(delta);
        //this.updateMagic(delta);
        this.updateHudLife(delta);
        this.updateHudArgent();
        this.updateChest(delta);

        this.checkCollision();

        // RENDER
        ScreenUtils.clear(0, 0, 0, 1);
        this.engine.getBatch().setProjectionMatrix(this.engine.getCamera().combined);
        this.tileMap.setView(this.engine.getCamera());
        this.tileMap.render();
        this.engine.getBatch().begin();

        this.renderChest();
        this.renderCoin();
        this.renderPopo();
        this.renderMaskull();
        this.renderLittle();
        this.renderBigZombie();
        this.renderBigDevil();
        //this.renderMage();
        this.renderBow();
        //this.renderStaff();
        this.renderArrow();
        //this.renderMagic();
        this.renderBow();
        this.renderHero();
        this.renderHudLife();
        this.renderHudArgent();

        this.engine.getBatch().end();
        if (this.engine.getInputManager().isPressed(GamePadAction.DRAW_HITBOX)) {
            this.drawHitboxes = !this.drawHitboxes;
        }
        if (this.drawHitboxes) {
            this.drawHitBox(this.engine.getCamera());
        }
        if(this.engine.getEntityManager().findByName("hero").getComponent(Vie.class).getVie() == 0) this.gameOver();
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

    public void gameOver() {
        Resource.DEAD_SOUND.play();
        this.engine.getEntityManager().findByName("camera").getComponent(MusicLevel.class).stop();
        this.engine.clear();
        this.engine.setScreen(new EndScreen(this.engine));
        this.dispose();
    }

    @Override
    public void dispose() {
        this.tileMap.dispose();
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

        direction.getValue().nor();

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
            hitBox.setX(heroHitBox.getX() + 1f);
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

//    private void updateStaff(float deltaTime) {
//        List<Entity> enemies = new ArrayList<>();
//        for (Entity e : this.engine.getEntityManager().getEntities()) {
//            if (e.getName().contains("mage")) {
//                enemies.add(e);
//            }
//        }
//
//        List<Entity> staffs = new ArrayList<>();
//        for (Entity e : this.engine.getEntityManager().getEntities()) {
//            if (e.getName().contains("staff")) {
//                staffs.add(e);
//            }
//        }
//        int index = 0;
//        for (Entity mage : enemies) {
//            Direction direction = mage.getComponent(Direction.class);
//            HitBox mageHitBox = mage.getComponent(HitBox.class);
//
//            Entity staff = this.engine.getEntityManager().findByName(staffs.get(index).getName());
//            index++;
//            AnimatedSpriteList animatedSpriteList = staff.getComponent(AnimatedSpriteList.class);
//            HitBox hitBox = staff.getComponent(HitBox.class);
//
//            if (direction.getValue().x > 0) {
//                hitBox.setX(mageHitBox.getX() + 0.5f);
//            }
//
//            if (direction.getValue().x < 0) {
//                hitBox.setX(mageHitBox.getX() -0.6f);
//            }
//
//            hitBox.setY(mageHitBox.getY());
//            animatedSpriteList.update(deltaTime);
//        }
//
//    }
//
//    private void renderStaff() {
//
//        List<Entity> enemies = new ArrayList<>();
//        for (Entity e : this.engine.getEntityManager().getEntities()) {
//            if (e.getName().contains("mage")) {
//                enemies.add(e);
//            }
//        }
//
//        List<Entity> staffs = new ArrayList<>();
//        for (Entity e : this.engine.getEntityManager().getEntities()) {
//            if (e.getName().contains("staff")) {
//                staffs.add(e);
//            }
//        }
//
//        int index = 0;
//        for (Entity mage : enemies) {
//            AnimatedSpriteList heroAnimatedSpriteList = mage.getComponent(AnimatedSpriteList.class);
//
//            Entity staff = this.engine.getEntityManager().findByName(staffs.get(index).getName());
//            index++;
//            AnimatedSpriteList animatedSpriteList = staff.getComponent(AnimatedSpriteList.class);
//            HitBox hitBox = staff.getComponent(HitBox.class);
//
//            TextureRegion sprite = animatedSpriteList.getFrame(true);
//
//            if (heroAnimatedSpriteList.isFlipX() && !sprite.isFlipX()) {
//                sprite.flip(true, false);
//            }
//            if (!heroAnimatedSpriteList.isFlipX() && sprite.isFlipX()) {
//                sprite.flip(true, false);
//            }
//
//            this.engine.getBatch().draw(
//                    sprite,
//                    hitBox.getX(),
//                    hitBox.getY(),
//                    sprite.getRegionWidth() / 16f,
//                    sprite.getRegionHeight() / 16f
//            );
//        }
//
//    }

    private void updateArrow(float deltaTime) {
        Entity bow = this.engine.getEntityManager().findByName("bow");

        Cursor cursor = this.engine.getInputManager().getCursor();

        long currentTime = System.currentTimeMillis();
        if (cursor.isPressed() && currentTime - timerTire > Constante.TEMPS_ENTRE_DEUX_TIRE) {
            bow.getComponent(SoundPlayer.class).getSound().play(0.3f);
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

//    private void updateMagic(float deltaTime) {
//
//        List<Entity> enemies = new ArrayList<>();
//        for (Entity e : this.engine.getEntityManager().getEntities()) {
//            if (e.getName().contains("mage")) {
//                enemies.add(e);
//            }
//        }
//
//        Entity hero = this.engine.getEntityByName("hero");
//
//        List<Entity> staffs = new ArrayList<>();
//        for (Entity e : this.engine.getEntityManager().getEntities()) {
//            if (e.getName().contains("staff")) {
//                staffs.add(e);
//            }
//        }
//        long currentTime = System.currentTimeMillis();
//        int index = 0;
//        for (Entity mage : enemies) {
//
//            Vector2 direction = hero.getComponent(Direction.class)
//                    .getValue()
//                    .sub(mage.getComponent(Direction.class).getValue());
//
//            HitBox mageHitBox = mage.getComponent(HitBox.class);
//
//            if (currentTime - this.timerEnemyAttack.get(index) <= System.currentTimeMillis()) {
//                Entity magic = EntityFactory.createMagic(
//                        "magic-" + UUID.randomUUID(),
//                        "enemiesAttack",
//                        mageHitBox.getX(),
//                        mageHitBox.getY(),
//                        0.5f,
//                        0.5f,
//                        direction.nor().scl(Constante.VELOCITY_ARROW)
//                );
//                this.engine.getEntityManager().add(magic);
//                this.timerEnemyAttack.set(index, currentTime + 10000000);
//                index++;
//            }
//        }
//
//        List<Entity> magics = this.engine.getEntityManager().findByGroupName("magics");
//        for (Entity magic: magics) {
//            HitBox hitBox = magic.getComponent(HitBox.class);
//            Trajectory trajectory = magic.getComponent(Trajectory.class);
//            Vector2 v = new Vector2(hitBox.getX(), hitBox.getY());
//            v.add(trajectory.getVector());
//            hitBox.setPosition(v.x, v.y);
//        }
//    }
//
//    private void renderMagic() {
//        List<Entity> magics = this.engine.getEntityManager().findByGroupName("magics");
//
//        for (Entity magic : magics) {
//            HitBox hitBox = magic.getComponent(HitBox.class);
//            StaticSprite sprite = magic.getComponent(StaticSprite.class);
//            TextureRegion texture = sprite.getTextureRegion();
//            Trajectory trajectory = magic.getComponent(Trajectory.class);
//            float x = hitBox.getX();
//            float y = hitBox.getY();
//            float w = texture.getRegionWidth() / 16f;
//            float h = texture.getRegionHeight() / 16f;
//            float ox = hitBox.getBox().getOriginX();
//            float oy = hitBox.getBox().getOriginY();
//            float sx = 1f;
//            float sy = 1f;
//            float r = trajectory.getAngle() - 90f;
//
//            this.engine.getBatch().draw(
//                    texture,
//                    x,
//                    y,
//                    ox,
//                    oy,
//                    w,
//                    h,
//                    sx,
//                    sy,
//                    r
//            );
//        }
//    }

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
                enemy.getComponent(SoundPlayer.class).getSound().play();
                Entity coin = EntityFactory.createCoin("coin-" + UUID.randomUUID(), hitBox.getX(), hitBox.getY());
                this.engine.getEntityManager().add(coin);

                Random random = new Random();
                float proba = random.nextFloat();

                enemy.addComponent(CollisionStatus.MARK_AS_REMOVE);
            }

            TimerManager timers = enemy.getComponent(TimerManager.class);

            if (timers.isActif("wait")) {
                long currenTime = System.currentTimeMillis();
                if (currenTime - timers.getLastTimeOf("wait") > 1000) {
                    timers.setActif("wait",false);
                }
            } else {
                hitBox.getOldPosition().set(hitBox.getX(), hitBox.getY());
                if (Intersector.overlaps(vision.getValue(), heroHitBox.getBox().getBoundingRectangle())) {

                    Vector2 posHero = new Vector2(heroHitBox.getX(), heroHitBox.getY());
                    Vector2 posMaskull = new Vector2(hitBox.getX(), hitBox.getY());
                    vision.getValue().set(posMaskull, vision.getValue().radius);
                    direction.getValue().set(posHero.sub(posMaskull));

                } else {
                    Random random = new Random();
                    Position positon = enemy.getComponent(Position.class);

                    long currentTime = System.currentTimeMillis();
                    if ( currentTime - timers.getLastTimeOf("move") > 3000) {
                        positon.getValue().x = -1 + random.nextFloat() * 2;
                        positon.getValue().y = -1 + random.nextFloat() * 2;
                        timers.setLastTimeOf("move", currentTime);
                    }

                    direction.getValue().x = positon.getValue().x;
                    direction.getValue().y = positon.getValue().y;
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

    private void updateLittle(float deltaTime) {
        List<Entity> enemies = new ArrayList<>();
        for (Entity e : this.engine.getEntityManager().getEntities()) {
            if (e.getName().contains("little")) {
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
                enemy.getComponent(SoundPlayer.class).getSound().play();
                Entity coin = EntityFactory.createCoin("coin-" + UUID.randomUUID(), hitBox.getX(), hitBox.getY());
                this.engine.getEntityManager().add(coin);

                Random random = new Random();
                float proba = random.nextFloat();

                if ( proba <= 0.1) {
                    Entity popo = EntityFactory.createHealPotion("chest-" + UUID.randomUUID(),
                            new Vector2(hitBox.getX(), hitBox.getY()), 0.6f, 0.7f);
                    this.engine.getEntityManager().add(popo);
                }
                enemy.addComponent(CollisionStatus.MARK_AS_REMOVE);
            }

            TimerManager timers = enemy.getComponent(TimerManager.class);

            if (timers.isActif("wait")) {
                long currenTime = System.currentTimeMillis();
                if (currenTime - timers.getLastTimeOf("wait") > 1000) {
                    timers.setActif("wait",false);
                }
            } else {
                hitBox.getOldPosition().set(hitBox.getX(), hitBox.getY());
                if (Intersector.overlaps(vision.getValue(), heroHitBox.getBox().getBoundingRectangle())) {

                    Vector2 posHero = new Vector2(heroHitBox.getX(), heroHitBox.getY());
                    Vector2 posLittle = new Vector2(hitBox.getX(), hitBox.getY());
                    vision.getValue().set(posLittle, vision.getValue().radius);
                    direction.getValue().set(posHero.sub(posLittle));

                    direction.getValue().nor();
                    direction.getValue().rotateDeg(180);
                    direction.getValue().setLength2(0.01f);

                } else {
                    Random random = new Random();
                    Position positon = enemy.getComponent(Position.class);

                    long currentTime = System.currentTimeMillis();
                    if ( currentTime - timers.getLastTimeOf("move") > 3000) {
                        positon.getValue().x = -1 + random.nextFloat() * 2;
                        positon.getValue().y = -1 + random.nextFloat() * 2;
                        timers.setLastTimeOf("move", currentTime);
                    }

                    direction.getValue().x = positon.getValue().x;
                    direction.getValue().y = positon.getValue().y;

                    direction.getValue().nor();
                    direction.getValue().setLength2(0.007f);
                }


                if (direction.getValue().x == 0 && direction.getValue().y == 0) {
                    animatedSpriteList.setCurrentAnimationName("idle");
                } else {
                    animatedSpriteList.setCurrentAnimationName("run");
                }

                hitBox.setX(hitBox.getX() + direction.getValue().x);
                vision.getValue().x = hitBox.getX();
                hitBox.setY(hitBox.getY() + direction.getValue().y);
                vision.getValue().y = hitBox.getY();

                if (direction.getValue().x > 0) {
                    animatedSpriteList.setFlipX(true);
                }
                if (direction.getValue().x < 0) {
                    animatedSpriteList.setFlipX(false);
                }
            }
        }
    }

    private void renderLittle() {
        List<Entity> enemies = new ArrayList<>();
        for (Entity e : this.engine.getEntityManager().getEntities()) {
            if (e.getName().contains("little")) {
                enemies.add(e);
            }
        }
        for (Entity enemy : enemies) {

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

    private void updateBigZombie(float deltaTime) {
        List<Entity> enemies = new ArrayList<>();
        for (Entity e : this.engine.getEntityManager().getEntities()) {
            if (e.getName().contains("big_zombie")) {
                enemies.add(e);
            }
        }
        Entity hero = this.engine.getEntityByName("hero");
        for (Entity enemy : enemies) {

            AnimatedSpriteList animatedSpriteList = enemy.getComponent(AnimatedSpriteList.class);
            animatedSpriteList.update(deltaTime);

            Direction direction = enemy.getComponent(Direction.class);
            HitBox hitBox = enemy.getComponent(HitBox.class);
            HitBox heroHitBox = hero.getComponent(HitBox.class);
            Velocity velocity = enemy.getComponent(Velocity.class);
            Vision vision = enemy.getComponent(Vision.class);
            Vie vie = enemy.getComponent(Vie.class);

            if (vie.getVie() == 0) {
                enemy.getComponent(SoundPlayer.class).getSound().play(0.1f);
                Entity coin1 = EntityFactory.createCoin("coin-" + UUID.randomUUID(), hitBox.getX(), hitBox.getY());
                Entity coin2 = EntityFactory.createCoin("coin-" + UUID.randomUUID(), hitBox.getX() + 0.5f, hitBox.getY());
                Entity coin3 = EntityFactory.createCoin("coin-" + UUID.randomUUID(), hitBox.getX() + 1f, hitBox.getY());
                this.engine.getEntityManager().add(coin1);
                this.engine.getEntityManager().add(coin2);
                this.engine.getEntityManager().add(coin3);

                Random random = new Random();
                float proba = random.nextFloat();

                if ( proba <= 0.1) {
                    Entity chest = EntityFactory.createChest("chest-" + UUID.randomUUID(),
                            new Vector2(hitBox.getX(), hitBox.getY()), 1f, 1f);
                    this.engine.getEntityManager().add(chest);
                }

                enemy.addComponent(CollisionStatus.MARK_AS_REMOVE);
            }

            TimerManager timers = enemy.getComponent(TimerManager.class);

            if (timers.isActif("wait")) {
                long currenTime = System.currentTimeMillis();
                if (currenTime - timers.getLastTimeOf("wait") > 1000) {
                    timers.setActif("wait",false);
                }
            } else {
                hitBox.getOldPosition().set(hitBox.getX(), hitBox.getY());
                if (Intersector.overlaps(vision.getValue(), heroHitBox.getBox().getBoundingRectangle())) {

                    Vector2 posHero = new Vector2(heroHitBox.getX(), heroHitBox.getY());
                    Vector2 posBigZombie = new Vector2(hitBox.getX(), hitBox.getY());
                    vision.getValue().set(posBigZombie, vision.getValue().radius);
                    direction.getValue().set(posHero.sub(posBigZombie));

                } else {
                    direction.getValue().x = 0;
                    direction.getValue().y = 0;
                }

                if (direction.getValue().x == 0 && direction.getValue().y == 0) {
                    animatedSpriteList.setCurrentAnimationName("idle");
                } else {
                    animatedSpriteList.setCurrentAnimationName("run");
                }

                direction.getValue().nor();
                direction.getValue().setLength2(0.012f);

                hitBox.setX(hitBox.getX() + direction.getValue().x);
                vision.getValue().x += direction.getValue().x;
                hitBox.setY(hitBox.getY() + direction.getValue().y);
                vision.getValue().y += direction.getValue().y;

                if (direction.getValue().x > 0) {
                    animatedSpriteList.setFlipX(false);
                }
                if (direction.getValue().x < 0) {
                    animatedSpriteList.setFlipX(true);
                }
            }
        }
    }

    private void renderBigZombie() {
        List<Entity> enemies = new ArrayList<>();
        for (Entity e : this.engine.getEntityManager().getEntities()) {
            if (e.getName().contains("big_zombie")) {
                enemies.add(e);
            }
        }
        for (Entity enemy : enemies) {

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
                    hitBox.getX() - 0.5f,
                    hitBox.getY(),
                    sprite.getRegionWidth() / 16f,
                    sprite.getRegionHeight() / 16f
            );

        }
    }

    private void updateBigDevil(float deltaTime) {
        List<Entity> enemies = new ArrayList<>();
        for (Entity e : this.engine.getEntityManager().getEntities()) {
            if (e.getName().contains("big_devil")) {
                enemies.add(e);
            }
        }
        Entity hero = this.engine.getEntityByName("hero");
        for (Entity enemy : enemies) {

            AnimatedSpriteList animatedSpriteList = enemy.getComponent(AnimatedSpriteList.class);
            animatedSpriteList.update(deltaTime);

            Direction direction = enemy.getComponent(Direction.class);
            HitBox hitBox = enemy.getComponent(HitBox.class);
            HitBox heroHitBox = hero.getComponent(HitBox.class);
            Velocity velocity = enemy.getComponent(Velocity.class);
            Vision vision = enemy.getComponent(Vision.class);
            Vie vie = enemy.getComponent(Vie.class);

            if (vie.getVie() == 0) {
                enemy.getComponent(SoundPlayer.class).getSound().play(0.1f);
                Entity chest = EntityFactory.createChest("chest-" + UUID.randomUUID(),
                        new Vector2(hitBox.getX(), hitBox.getY()), 1f, 1f);
                this.engine.getEntityManager().add(chest);
                enemy.addComponent(CollisionStatus.MARK_AS_REMOVE);
            }

            TimerManager timers = enemy.getComponent(TimerManager.class);

            if (timers.isActif("wait")) {
                long currenTime = System.currentTimeMillis();
                if (currenTime - timers.getLastTimeOf("wait") > 1000) {
                    timers.setActif("wait",false);
                }
            } else {
                hitBox.getOldPosition().set(hitBox.getX(), hitBox.getY());
                if (Intersector.overlaps(vision.getValue(), heroHitBox.getBox().getBoundingRectangle())) {

                    Vector2 posHero = new Vector2(heroHitBox.getX(), heroHitBox.getY());
                    Vector2 posBigDevil = new Vector2(hitBox.getX(), hitBox.getY());
                    direction.getValue().set(posHero.sub(posBigDevil));
                    vision.getValue().set(posBigDevil, vision.getValue().radius);

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
    }

    private void renderBigDevil() {
        List<Entity> enemies = new ArrayList<>();
        for (Entity e : this.engine.getEntityManager().getEntities()) {
            if (e.getName().contains("big_devil")) {
                enemies.add(e);
            }
        }
        for (Entity enemy : enemies) {

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
                    hitBox.getX() - 0.5f,
                    hitBox.getY(),
                    sprite.getRegionWidth() / 16f,
                    sprite.getRegionHeight() / 16f
            );

        }
    }

//    private void updateMage(float deltaTime) {
//        List<Entity> enemies = new ArrayList<>();
//        for (Entity e : this.engine.getEntityManager().getEntities()) {
//            if (e.getName().contains("mage")) {
//                enemies.add(e);
//            }
//        }
//
//        Entity hero = this.engine.getEntityByName("hero");
//        for (Entity enemy : enemies) {
//            //enemy = this.engine.getEntityByName("maskull" + cpt);
//            AnimatedSpriteList animatedSpriteList = enemy.getComponent(AnimatedSpriteList.class);
//            animatedSpriteList.update(deltaTime);
//
//            Direction direction = enemy.getComponent(Direction.class);
//            HitBox hitBox = enemy.getComponent(HitBox.class);
//            HitBox heroHitBox = hero.getComponent(HitBox.class);
//            Velocity velocity = enemy.getComponent(Velocity.class);
//            Vision vision = enemy.getComponent(Vision.class);
//            Vie vie = enemy.getComponent(Vie.class);
//
//            if (vie.getVie() == 0) {
//                enemy.addComponent(CollisionStatus.MARK_AS_REMOVE);
//            }
//
//            TimerManager timers = enemy.getComponent(TimerManager.class);
//
//            if (timers.isActif("wait")) {
//                long currenTime = System.currentTimeMillis();
//                if (currenTime - timers.getLastTimeOf("wait") > 1000) {
//                    timers.setActif("wait",false);
//                }
//            } else {
//                hitBox.getOldPosition().set(hitBox.getX(), hitBox.getY());
//                if (Intersector.overlaps(vision.getValue(), heroHitBox.getBox().getBoundingRectangle())) {
//
//                    Vector2 posHero = new Vector2(heroHitBox.getX(), heroHitBox.getY());
//                    Vector2 posMaskull = new Vector2(hitBox.getX(), hitBox.getY());
//
//                    direction.getValue().set(posHero.sub(posMaskull));
//
//                } else {
//                    direction.getValue().x = 0;
//                    direction.getValue().y = 0;
//                }
//
//                if (direction.getValue().x != 0 && direction.getValue().y != 0) {
//                    direction.getValue().nor();
//                }
//
//                if (direction.getValue().x == 0 && direction.getValue().y == 0) {
//                    animatedSpriteList.setCurrentAnimationName("idle");
//                } else {
//                    animatedSpriteList.setCurrentAnimationName("run");
//                }
//
//                hitBox.setX(hitBox.getX() - direction.getValue().x * velocity.getValue());
//                vision.getValue().x -= direction.getValue().x * velocity.getValue();
//                hitBox.setY(hitBox.getY() - direction.getValue().y * velocity.getValue());
//                vision.getValue().y -= direction.getValue().y * velocity.getValue();
//
//                if (direction.getValue().x > 0) {
//                    animatedSpriteList.setFlipX(false);
//                }
//                if (direction.getValue().x < 0) {
//                    animatedSpriteList.setFlipX(true);
//                }
//            }
//        }
//    }
//
//    private void renderMage() {
//        List<Entity> enemies = new ArrayList<>();
//        for (Entity e : this.engine.getEntityManager().getEntities()) {
//            if (e.getName().contains("mage")) {
//                enemies.add(e);
//            }
//        }
//        for (Entity enemy : enemies) {
//
//            AnimatedSpriteList animatedSpriteList = enemy.getComponent(AnimatedSpriteList.class);
//            TextureRegion sprite = animatedSpriteList.getFrame(true);
//            if (animatedSpriteList.isFlipX() && !sprite.isFlipX()) {
//                sprite.flip(true, false);
//            }
//
//            if (!animatedSpriteList.isFlipX() && sprite.isFlipX()) {
//                sprite.flip(true, false);
//            }
//
//            HitBox hitBox = enemy.getComponent(HitBox.class);
//            this.engine.getBatch().draw(
//                    sprite,
//                    hitBox.getX(),
//                    hitBox.getY(),
//                    sprite.getRegionWidth() / 16f,
//                    sprite.getRegionHeight() / 16f
//            );
//
//        }
//    }


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

        int pos = 0;

        // Dessin des coeurs plein
        for (int i = 0; i < vieHero.getVie()  / 2; i++) {
            this.engine.getBatch().draw(hud.getFullHeart().getTexture(),
                    posHud.getValue().x + (float) pos * 1.5f,
                    posHud.getValue().y,
                    hud.getWidth(),
                    hud.getHeight());
            pos++;
        }

        // Dessin des demi-coeur
        if (vieHero.getVie()  % 2 > 0) {
            this.engine.getBatch().draw(hud.getHalfHeart().getTexture(),
                    posHud.getValue().x + (float) pos * 1.5f,
                    posHud.getValue().y,
                    hud.getWidth(),
                    hud.getHeight());
            pos++;
        }

        // Dessin des coeurs vide
        for (int i = 0; i < (Constante.VIE_HERO_MAX - vieHero.getVie()) / 2; i++) {
            this.engine.getBatch().draw(hud.getEmptyHeart().getTexture(),
                    posHud.getValue().x + (float) pos * 1.5f,
                    posHud.getValue().y,
                    hud.getWidth(),
                    hud.getHeight());
            pos++;
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

    private void renderPopo() {
        List<Entity> potions = this.engine.getEntityManager().findByGroupName("potions");

        for (Entity e: potions) {
            StaticSprite sprite = e.getComponent(StaticSprite.class);
            HitBox hitBox = e.getComponent(HitBox.class);

            this.engine.getBatch().draw(sprite.getTexture(),
                    hitBox.getX(),
                    hitBox.getY(),
                    hitBox.getWidth(),
                    hitBox.getHeight());
        }
    }

    private void renderChest() {
        List<Entity> coffres = this.engine.getEntityManager().findByGroupName("coffres");

        for (Entity e: coffres) {
            AnimatedSpriteList animatedSpriteList = e.getComponent(AnimatedSpriteList.class);
            HitBox hitBox = e.getComponent(HitBox.class);

            TextureRegion sprite = animatedSpriteList.getFrame(false);

            this.engine.getBatch().draw(sprite,
                    hitBox.getX(),
                    hitBox.getY(),
                    hitBox.getWidth(),
                    hitBox.getHeight());
        }
    }

    private void updateChest(float deltaTime) {
        List<Entity> coffres = this.engine.getEntityManager().findByGroupName("coffres");

        for (Entity e: coffres) {
            AnimatedSpriteList animatedSpriteList = e.getComponent(AnimatedSpriteList.class);
            HitBox hitBox = e.getComponent(HitBox.class);
            TimerManager timerManager = e.getComponent(TimerManager.class);
            Vie vie = e.getComponent(Vie.class);

            animatedSpriteList.update(deltaTime);

            if (vie.getVie() == 0) {
                animatedSpriteList.setCurrentAnimationName("opening");
                if (hitBox.isActive()) {
                    timerManager.resetOf("destruction");
                    timerManager.setActif("destruction", true);
                    hitBox.setActity(false);

                    // Création du loot: entre 5 et 10 pièces et une potion avec une proba de 1/3
                    Random random = new Random();

                    for (int i = 0; i < random.nextInt(6) + 5; i++) {
                        float posX = (hitBox.getX() - 1f) + random.nextFloat() * ((hitBox.getX() + 2f) - (hitBox.getX() - 1f));
                        float posY = (hitBox.getY() - 1f) + random.nextFloat() * ((hitBox.getY() + 2f) - (hitBox.getY() - 1f));
                        Entity coin = EntityFactory.createCoin("coin-" + UUID.randomUUID(),
                                posX, posY);
                        this.engine.getEntityManager().add(coin);
                    }

                    float proba = random.nextFloat();

                    if (proba < 0.33333) {
                        float posX = (hitBox.getX() - 1f) + random.nextFloat() * ((hitBox.getX() + 2f) - (hitBox.getX() - 1f));
                        float posY = (hitBox.getY() - 1f) + random.nextFloat() * ((hitBox.getY() + 2f) - (hitBox.getY() - 1f));
                        Vector2 posPopo = new Vector2(posX, posY);
                        Entity popo = EntityFactory.createHealPotion("potion-" + UUID.randomUUID(), posPopo, 0.6f, 0.7f);
                        this.engine.getEntityManager().add(popo);
                    }
                }


                // Le coffre disparait après 3 secondes
                if (!hitBox.isActive() && timerManager.isActif("destruction")) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - timerManager.getLastTimeOf("destruction") > 3000) {
                        e.addComponent(CollisionStatus.MARK_AS_REMOVE);
                        timerManager.setActif("destruction", false);
                    }
                }
            } else {
                animatedSpriteList.setCurrentAnimationName("close");
            }

        }
    }
}
