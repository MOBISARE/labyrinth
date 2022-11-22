package com.mygdx.labyrinth.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.labyrinth.Resource;
import com.mygdx.labyrinth.entity.component.*;


import java.util.Map;

public final class EntityFactory {

    public static final float COIN_SIZE = 1/2f;

    public static final float HERO_WIDTH = 1f;

    public static final float HERO_HEIGHT = 1f;

    public static final float MASKULL_WIDTH = HERO_WIDTH;

    public static final float MASKULL_HEIGHT = HERO_HEIGHT;

    public static final float ARROW_DISTANCE = 5f;

    private EntityFactory() {

    }

    public static Entity createCoin(String name, float randomXPosition, float randomYPosition) {
        Entity entity = new Entity(name, "coins");
        entity.addComponent(new HitBox(randomXPosition, randomYPosition, COIN_SIZE, COIN_SIZE, true, false));
        entity.addComponent(new AnimatedSprite(Resource.COIN_TEXTURE, 4, 1, 0.12f));
        entity.addComponent(new SoundPlayer(Resource.COIN_SOUND));
        entity.addComponent(CollisionStatus.NONE);
        return entity;
    }

    public static Entity createHero(float startXPosition, float startYPosition) {
        Entity entity = new Entity("hero");

        Map<String, AnimatedSpriteList.AnimationData> animations = Map.of(
                "idle", new AnimatedSpriteList.AnimationData(0.15f, 1, 5),
                "run", new AnimatedSpriteList.AnimationData(0.105f, 5, 9)
        );

        entity.addComponent(new AnimatedSpriteList(Resource.HERO_TEXTURE, 9, 1, animations));
        HitBox hb = new HitBox(startXPosition, startYPosition, 0.7f, 1.2f, true, true);
        entity.addComponent(hb);
        entity.addComponent(new Direction(0, 0));
        entity.addComponent(new Velocity(0.1f));
        entity.addComponent(new SoundPlayer(Resource.HERO_WALK_SOUND, Resource.HERO_WALK_DELTASOUND));
        entity.addComponent(new DynamicBody());
        entity.addComponent(CollisionStatus.NONE);
        entity.addComponent(new Vie(6));
        Argent arg = new Argent(0);
        entity.addComponent(arg);

        CollisionHandler collisionHandler = (e1, e2) -> {

            if (e2.getGroupName().equals("coins")) {
                arg.setArgent(arg.getArgent() + 1);
                e2.addComponent(CollisionStatus.MARK_AS_REMOVE);
            }

            switch (e2.getName()) {
                case "topWall":
                case "botWall":
                    hb.setY(hb.getOldPosition().y);
                    break;
                case "leftWall":
                case "rightWall":
                    hb.setX(hb.getOldPosition().x);
                    break;
                case "maskull":
                    e2.getComponent(HitBox.class).setPosition(e2.getComponent(HitBox.class).getOldPosition().x,
                            e2.getComponent(HitBox.class).getOldPosition().y);
                    e2.getComponent(Vision.class).getValue().setPosition(e2.getComponent(HitBox.class).getOldPosition());
                    break;
            }
        };

        entity.setCollisionHandler(collisionHandler);

        return entity;
    }

    public static Entity createCamera() {
        Entity entity = new Entity("camera");
        entity.addComponent(new FollowingCamera());
        entity.addComponent(new MusicLevel(Resource.MUSIC_LEVEL));
        return entity;
    }

    public static Entity createMaskull(String name, float startXPosition, float startYPosition) {
        Entity entity = new Entity(name, "enemies");
        Map<String, AnimatedSpriteList.AnimationData> animations = Map.of(
                "idle", new AnimatedSpriteList.AnimationData(0.15f, 0, 4),
                "run", new AnimatedSpriteList.AnimationData(0.105f, 4, 8)
        );
        entity.addComponent(new AnimatedSpriteList(Resource.MASKULL_TEXTURE, 8, 1, animations));
        entity.addComponent(new HitBox(startXPosition, startYPosition, 0.9f, 1f, true, true));
        entity.addComponent(new Direction(0, 0));
        entity.addComponent(new Velocity(0.05f));
        entity.addComponent(new Vision(
                startXPosition ,
                startYPosition,
                10f));
        entity.addComponent(new DynamicBody());
        entity.addComponent(CollisionStatus.NONE);
        Vie vie = new Vie(3);
        entity.addComponent(vie);

        return entity;
    }

    public static Entity createWall(String name, float positionX, float positionY, float width, float height) {
        Entity entity = new Entity(name, "walls");

        entity.addComponent(new HitBox(positionX, positionY, width, height, true, false));
        entity.addComponent(CollisionStatus.NONE);
        return entity;
    }

    public static Entity createBow(String name, float positionX, float positionY, float width, float height) {
        Entity entity = new Entity(name);

        Map<String, AnimatedSpriteList.AnimationData> animations = Map.of(
                "idle", new AnimatedSpriteList.AnimationData(0.105f, 0, 1)
        );

        entity.addComponent(new HitBox(positionX, positionY, width, height, false, false));
        entity.addComponent(new AnimatedSpriteList(Resource.BOW_TEXTURE, 1, 1, animations));
        return entity;
    }

    public static Entity createArrow(String name,
                                     String groupName,
                                     float positionX,
                                     float positionY,
                                     float width,
                                     float height,
                                     Vector2 direction) {
        Entity entity = new Entity(name, groupName);
        HitBox hb = new HitBox(positionX, positionY, width, height, true, true);
        entity.addComponent(hb);
        entity.addComponent(new StaticSprite(Resource.ARROW_TEXTURE));
        entity.addComponent(new Velocity(0.3f));
        entity.addComponent(new DynamicBody());
        Trajectory tj = new Trajectory(direction , ARROW_DISTANCE);
        entity.addComponent(tj);
        entity.addComponent(CollisionStatus.NONE);
        hb.getBox().setRotation(tj.getAngle() - 90f);

        CollisionHandler collisionHandler = (e1, e2) -> {
            if (e2.getName().equals("maskull")) {
                entity.addComponent(CollisionStatus.MARK_AS_REMOVE);
                Vie vie = e2.getComponent(Vie.class);
                vie.setVie(vie.getVie() - 1);
                System.out.println("touché");
            }
        };

        entity.setCollisionHandler(collisionHandler);
        return entity;

    }

    public static Entity createLifeHud(String name, Vector2 pos, float width, float height) {
        Entity entity = new Entity(name);

        entity.addComponent(new Position(pos.x, pos.y));
        StaticSprite[] sprites = {new StaticSprite(Resource.FULL_HEART),
                new StaticSprite(Resource.HALF_HEART),
                new StaticSprite(Resource.EMPTY_HEART)};
        entity.addComponent(new HudLife(sprites, width, height));

        return entity;
    }

    public static Entity createHudArgent(String name, Vector2 pos, float width, float height) {
        Entity entity = new Entity(name);

        entity.addComponent(new Dimension(height, width));
        entity.addComponent(new Position(pos.x, pos.y));
        entity.addComponent(new Font(Resource.FONT_HUD, 17, 0.5f, Color.YELLOW, 1/16f));
        entity.addComponent(new StaticSprite(Resource.IMAGE_COIN_HUD));

        return entity;
    }
}
