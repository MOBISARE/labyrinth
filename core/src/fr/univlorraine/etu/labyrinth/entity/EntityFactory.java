package fr.univlorraine.etu.labyrinth.entity;

import fr.univlorraine.etu.labyrinth.Resource;
import fr.univlorraine.etu.labyrinth.engine.Engine;
import fr.univlorraine.etu.labyrinth.entity.component.*;


import java.util.Map;
import java.util.UUID;

public final class EntityFactory {

    public static final float COIN_SIZE = 1/2f;

    public static final float HERO_WIDTH = 1f;

    public static final float HERO_HEIGHT = 1f;

    public static final float MASKULL_WIDTH = HERO_WIDTH;

    public static final float MASKULL_HEIGHT = HERO_HEIGHT;

    private EntityFactory() {

    }

    public static Entity createCoin(String name, float randomXPosition, float randomYPosition) {
        Entity entity = new Entity(name, "coins");
        entity.addComponent(new HitBox(randomXPosition, randomYPosition, COIN_SIZE, COIN_SIZE));
        entity.addComponent(new AnimatedSprite(Resource.COIN_TEXTURE, 4, 1, 0.12f));
        entity.addComponent(new SoundPlayer(Resource.COIN_SOUND));
        return entity;
    }

    public static Entity createHero(float startXPosition, float startYPosition) {
        Entity entity = new Entity("hero");

        Map<String, AnimatedSpriteList.AnimationData> animations = Map.of(
                "idle", new AnimatedSpriteList.AnimationData(0.15f, 1, 5),
                "run", new AnimatedSpriteList.AnimationData(0.105f, 5, 9)
        );

        entity.addComponent(new AnimatedSpriteList(Resource.HERO_TEXTURE, 9, 1, animations));
        entity.addComponent(new HitBox(startXPosition, startYPosition, 1, 1.25f));
        entity.addComponent(new Direction(0, 0));
        entity.addComponent(new Velocity(0.1f));
        entity.addComponent(new SoundPlayer(Resource.HERO_WALK_SOUND));
        entity.addComponent(new DynamicBody());

        return entity;
    }

    public static Entity createCamera() {
        Entity entity = new Entity("camera");
        entity.addComponent(new FollowingCamera());
        return entity;
    }

    public static Entity createMaskull(String name, float startXPosition, float startYPosition) {
        Entity entity = new Entity(name, "enemies");
        Map<String, AnimatedSpriteList.AnimationData> animations = Map.of(
                "idle", new AnimatedSpriteList.AnimationData(0.15f, 0, 4),
                "run", new AnimatedSpriteList.AnimationData(0.105f, 4, 8)
        );
        entity.addComponent(new AnimatedSpriteList(Resource.MASKULL_TEXTURE, 8, 1, animations));
        entity.addComponent(new HitBox(startXPosition, startYPosition, 1f, 1f));
        entity.addComponent(new Direction(0, 0));
        entity.addComponent(new Velocity(0.05f));
        entity.addComponent(new Vision(
                startXPosition ,
                startYPosition,
                10f));
        entity.addComponent(new DynamicBody());

        return entity;
    }

    public static Entity createWall(String name, float positionX, float positionY, float width, float height) {
        Entity entity = new Entity(name, "walls");
        entity.addComponent(new HitBox(positionX, positionY, width, height));
        return entity;
    }
}
