package fr.univlorraine.etu.labyrinth.screen;

import com.badlogic.gdx.Gdx;
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
import fr.univlorraine.etu.labyrinth.Resource;
import fr.univlorraine.etu.labyrinth.engine.Engine;
import fr.univlorraine.etu.labyrinth.entity.Entity;
import fr.univlorraine.etu.labyrinth.entity.EntityFactory;
import fr.univlorraine.etu.labyrinth.entity.component.*;
import fr.univlorraine.etu.labyrinth.input.ClickPosition;
import fr.univlorraine.etu.labyrinth.input.CursorAction;
import fr.univlorraine.etu.labyrinth.input.GamePadAction;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

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
        Entity maskull = EntityFactory.createMaskull("maskull", x + 3, y + 3);
        this.engine.getEntityManager().add(maskull);

        // CAMERA
        Entity camera = EntityFactory.createCamera();
        FollowingCamera followingCamera = camera.getComponent(FollowingCamera.class);
        followingCamera.init();
        this.engine.getEntityManager().add(camera);

        // WALLS
        Entity upWall = EntityFactory.createWall("upWall", 0, mapHeight - 1f, mapWidth, 1f);
        Entity downWall = EntityFactory.createWall("downWall", 0, 0, mapWidth, 1.25f);
        Entity leftWall = EntityFactory.createWall("leftWall", 0, 0, 0.25f, mapHeight);
        Entity rightWall = EntityFactory.createWall("rightWall", mapWidth - 0.25f, 0, 1f, mapHeight);
        this.engine.getEntityManager().add(upWall);
        this.engine.getEntityManager().add(downWall);
        this.engine.getEntityManager().add(leftWall);
        this.engine.getEntityManager().add(rightWall);


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

        this.engine.getBatch().end();
        this.drawHitBox(this.engine.getCamera());
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
    }

    private void drawHitBox(OrthographicCamera camera) {
        this.debbug.setProjectionMatrix(camera.combined);
        List<HitBox> hitBoxes = this.engine.getEntityManager().findOneByComponent(HitBox.class);
        List<Vision> visions = this.engine.getEntityManager().findOneByComponent(Vision.class);
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
                    hitBox.getValue().x,
                    hitBox.getValue().y,
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
            hitBox.getValue().x = heroHitBox.getValue().x + 1f;
        }

        if (heroDirection.getValue().x < 0) {
            hitBox.getValue().x = heroHitBox.getValue().x - 0.3f;
        }

        hitBox.getValue().y = heroHitBox.getValue().y;
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
                hitBox.getValue().x,
                hitBox.getValue().y,
                sprite.getRegionWidth() / 16f,
                sprite.getRegionHeight() / 16f
        );

    }

    private void updateArrow(float deltaTime) {
        Entity bow = this.engine.getEntityManager().findByName("bow");

        if (this.engine.getInputManager().getCursor().isButtonClicked(CursorAction.ATTACK)) {

            // Attention renvoie l'origine est en haut à gauche !!!
            ClickPosition cp = this.engine.getInputManager().getCursor().getLastPosition(CursorAction.ATTACK);

            HitBox bowHitBox = bow.getComponent(HitBox.class);
            float dx = cp.getX() - (bowHitBox.getValue().width / 2);
            float dy = cp.getY() - (bowHitBox.getValue().height / 2);

            // Récupération des coordonnée du clic (en pixel: origine en haut à gauche)
            Vector3 mouseTmp = new Vector3(cp.getX(), cp.getY(), 0f);
            // Pour récupérer les coordonnées on fonction du monde
            this.engine.getCamera().unproject(mouseTmp);
            Vector2 mouse = new Vector2(mouseTmp.x, mouseTmp.y);

            Vector2 centerBow = new Vector2(bowHitBox.getValue().x - bowHitBox.getValue().width / 2f,
                    bowHitBox.getValue().y + bowHitBox.getValue().height / 2f);


            Vector2 direction = centerBow.sub(mouse);

            Entity arrow = EntityFactory.createArrow(
                    "arrow-" + UUID.randomUUID(),
                    "heroArrows",
                    bowHitBox.getValue().x,
                    bowHitBox.getValue().y,
                    0.5f,
                    1f,
                    direction.nor().scl(0.2f)
            );
            this.engine.getEntityManager().add(arrow);
        }

        int mapWidth = this.map.getProperties().get("width", int.class);
        int mapHeight = this.map.getProperties().get("height", int.class);
        List<Entity> arrows = this.engine.getEntityManager().findByGroupName("heroArrows");
        //System.out.println("ARROWS COUNT : " + arrows.size());
        for (Entity a : arrows) {
            Velocity velocity = a.getComponent(Velocity.class);
            Trajectory trajectory = a.getComponent(Trajectory.class);
            HitBox box = a.getComponent(HitBox.class);
            Vector2 posBox = new Vector2(box.getValue().x, box.getValue().y);
            box.getValue().setPosition(posBox.sub(trajectory.getVector()));

            if (box.getValue().x > mapWidth || box.getValue().y > mapHeight) {
                a.addComponent(CollisionStatus.MARK_AS_REMOVE);
            }
        }


    }

    private void renderArrow() {
        List<Entity> arrows = this.engine.getEntityManager().findByGroupName("heroArrows");
        for (Entity arrow : arrows) {
            HitBox hitBox = arrow.getComponent(HitBox.class);
            StaticSprite sprite = arrow.getComponent(StaticSprite.class);
            TextureRegion texture = sprite.getTextureRegion();
            Trajectory trajectory = arrow.getComponent(Trajectory.class);
            float x = hitBox.getValue().x;
            float y = hitBox.getValue().y;
            float w = texture.getRegionWidth() / 16f;
            float h = texture.getRegionHeight() / 16f;
            float ox = w/2f;
            float oy = h/2f;
            float sx = 1f;
            float sy = 1f;
            float r = trajectory.getAngle() + 90f;

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
        Entity maskull = this.engine.getEntityByName("maskull");
        Entity hero = this.engine.getEntityByName("hero");
        AnimatedSpriteList animatedSpriteList = maskull.getComponent(AnimatedSpriteList.class);
        animatedSpriteList.update(deltaTime);

        Direction direction = maskull.getComponent(Direction.class);
        HitBox hitBox = maskull.getComponent(HitBox.class);
        HitBox heroHitBox = hero.getComponent(HitBox.class);
        Velocity velocity = maskull.getComponent(Velocity.class);
        Vision vision = maskull.getComponent(Vision.class);

        if (Intersector.overlaps(maskull.getComponent(Vision.class).getValue(),
                hero.getComponent(HitBox.class).getValue())) {

            direction.getValue().x -= hitBox.getValue().x - heroHitBox.getValue().x;
            direction.getValue().y -= hitBox.getValue().y - heroHitBox.getValue().y;

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
        vision.getValue().x += direction.getValue().x * velocity.getValue();
        hitBox.getValue().y += direction.getValue().y * velocity.getValue();
        vision.getValue().y += direction.getValue().y * velocity.getValue();

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
                sprite.getRegionWidth() / 16f,
                sprite.getRegionHeight() / 16f
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
                mapWidth,
                mapHeight
        );

        followingCamera.getCamera().update();
        this.engine.getBatch().setProjectionMatrix(followingCamera.getCamera().combined);
        this.tileMap.setView(followingCamera.getCamera());
    }

    private void checkCollision() {
//        List<HitBox> hitboxes = new ArrayList<>();
//        Set<Entity> dynamicBodies = this.engine.getEntityManager().getDynamicBodies();
//
//        for (Entity e : dynamicBodies) {
//            hitboxes.add(e.getComponent(HitBox.class));
//        }

        List<HitBox> hitBoxes = this.engine.getEntityManager().findOneByComponent(HitBox.class);

        // Check collisions entre des entités dynamiques
        for (HitBox h0 : hitBoxes) {
            for (HitBox h1 : hitBoxes) {
                if (!Objects.equals(h0.getValue(), h1.getValue())) {
                    if (h0.getValue().overlaps(h1.getValue())) {
                        this.handleCollision(h0, h1);
                    }
                }
            }
        }

        List<Entity> entities = this.engine
                .getEntityManager()
                .findByComponent(CollisionStatus.MARK_AS_REMOVE);

        //System.out.println(entities);
        this.engine.getEntityManager().remove(entities);

        // Check collisions entre des entités dynamiques et une entité


    }

    private void handleCollision(HitBox source, HitBox target) {

        Entity sourceEntity = this.engine.getEntityManager().findOneByComponent(source);
        Entity targetEntity = this.engine.getEntityManager().findOneByComponent(target);

        // Collision avec le champ de vision d'un ennemi
        if (sourceEntity.hasComponent(Vision.class)
                && Intersector.overlaps(sourceEntity.getComponent(Vision.class).getValue(),
                target.getValue())) {
            Vision sourceVision = sourceEntity.getComponent(Vision.class);
            this.handleCollisionHeroToEnemyVision(sourceVision, target);
            targetEntity.addComponent(CollisionStatus.SPOTTED);
        } else {
            targetEntity.addComponent(CollisionStatus.NONE);
        }

        // Collision avec une pièce
        if (Objects.equals(targetEntity.getGroupName(), "coins")) {
            this.handleCollisionHeroToCoin(source, target);
        }

        // Collision d'une entité mobile avec un mur
        if ((Objects.equals(sourceEntity.getName(), "hero")
                || Objects.equals(sourceEntity.getGroupName(), "enemies"))
                && Objects.equals(targetEntity.getGroupName(), "walls")) {
            this.handleCollisionToWall(source, target);
        }

        // Collision du héros avec un ennemi
        if (Objects.equals(sourceEntity.getGroupName(), "enemies")
                && Objects.equals(targetEntity.getName(), "hero")) {
            this.handleCollisionEnemyToHero(source, target);
        }

        //System.out.println("Collision entre " + sourceEntity.getName() + " et " + targetEntity.getName());

    }

    private void handleCollisionHeroToCoin(HitBox source, HitBox target) {
        Entity sourceEntity = this.engine.getEntityManager().findOneByComponent(source);
        Entity targetEntity = this.engine.getEntityManager().findOneByComponent(target);
        if (Objects.equals(sourceEntity.getName(), "hero")
                && Objects.equals(targetEntity.getGroupName(), "coins")) {
            targetEntity.addComponent(CollisionStatus.MARK_AS_REMOVE);
        }
    }

    // La méthode ci-dessous n'est pas forcément utile car elle ne fait qu'un print pour le débug
    // Cependant elle pourrait servir pour gérer le comportement des ennemis si le héros se trouve dans son champs de vision
    // Par exemple tire un projectile vers le héros
    private void handleCollisionHeroToEnemyVision(Vision source, HitBox target) {
        Entity sourceEntity = this.engine.getEntityManager().findOneByComponent(source);
        Entity targetEntity = this.engine.getEntityManager().findOneByComponent(target);
//
//        System.out.println("SOURCE " + sourceEntity.getName());
//        System.out.println("TARGET " + targetEntity.getName());
//        if (Objects.equals(targetEntity.getName(), "maskull")
//                && Objects.equals(sourceEntity.getName(), "hero")) {
//            sourceEntity.addComponent(CollisionStatus.SPOTTED);
//            System.out.println(sourceEntity.getName() + " a été repéré par " + targetEntity.getName());
//
//        } else {
//            sourceEntity.addComponent(CollisionStatus.NONE);
//        }
        //System.out.println(targetEntity.getName() + " a été repéré par " + sourceEntity.getName());
    }

    private void handleCollisionToWall(HitBox source, HitBox target) {
        Entity sourceEntity = this.engine.getEntityManager().findOneByComponent(source);
        Entity targetEntity = this.engine.getEntityManager().findOneByComponent(target);

        if (Objects.equals(targetEntity.getName(), "upWall")
                || Objects.equals(targetEntity.getName(), "downWall")) {
            source.getValue().y
                    -= sourceEntity.getComponent(Direction.class).getValue().y
                    * sourceEntity.getComponent(Velocity.class).getValue();

        } else if (Objects.equals(targetEntity.getName(), "rightWall")
                || Objects.equals(targetEntity.getName(), "leftWall")) {
            source.getValue().x
                    -= sourceEntity.getComponent(Direction.class).getValue().x
                    * sourceEntity.getComponent(Velocity.class).getValue();

        }

    }

    private void handleCollisionEnemyToHero(HitBox source, HitBox target) {
        Entity sourceEntity = this.engine.getEntityManager().findOneByComponent(source);
        Entity targetEntity = this.engine.getEntityManager().findOneByComponent(target);
        Vision sourceVision = this.engine.getEntityManager().findOneByComponent(source).getComponent(Vision.class);

        if (target.getValue().x < source.getValue().x
                || target.getValue().x > source.getValue().x) {

            source.getValue().x
                    -= sourceEntity.getComponent(Direction.class).getValue().x
                    * sourceEntity.getComponent(Velocity.class).getValue();

            sourceVision.getValue().x
                    -= sourceEntity.getComponent(Direction.class).getValue().x
                    * sourceEntity.getComponent(Velocity.class).getValue();
        }

        if (target.getValue().y < source.getValue().y
                || target.getValue().y > source.getValue().y) {

            source.getValue().y
                    -= sourceEntity.getComponent(Direction.class).getValue().y
                    * sourceEntity.getComponent(Velocity.class).getValue();

            sourceVision.getValue().y
                    -= sourceEntity.getComponent(Direction.class).getValue().y
                    * sourceEntity.getComponent(Velocity.class).getValue();
        }
    }

}
