package fr.univlorraine.etu.labyrinth.entity;

import fr.univlorraine.etu.labyrinth.Resource;
import fr.univlorraine.etu.labyrinth.engine.Engine;
import fr.univlorraine.etu.labyrinth.entity.component.*;


import java.util.Map;
import java.util.UUID;

public final class EntityFactory {

    public static final int COIN_SIZE = 8 * Engine.SCALE;

    public static final float HERO_WIDTH = 16f * Engine.SCALE;

    public static final float HERO_HEIGHT = 28f * Engine.SCALE;

    private EntityFactory() {

    }

    public static Entity createCoin(String name, float randomXPosition, float randomYPosition) {
        Entity entity = new Entity(name, "coin");
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
        entity.addComponent(new HitBox(startXPosition, startYPosition, HERO_WIDTH, HERO_HEIGHT - 8f * Engine.SCALE));
        entity.addComponent(new Direction(0, 0));
        entity.addComponent(new Velocity(5f));
        entity.addComponent(new SoundPlayer(Resource.HERO_WALK_SOUND));

        return entity;
    }

    public static Entity createCamera() {
        Entity entity = new Entity("camera");
        entity.addComponent(new FollowingCamera());
        return entity;
    }
}
