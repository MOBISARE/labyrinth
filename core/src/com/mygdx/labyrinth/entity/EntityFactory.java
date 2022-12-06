package com.mygdx.labyrinth.entity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.labyrinth.Constante;
import com.mygdx.labyrinth.Resource;
import com.mygdx.labyrinth.entity.component.camera.FollowingCamera;
import com.mygdx.labyrinth.entity.component.collisions.CollisionHandler;
import com.mygdx.labyrinth.entity.component.collisions.CollisionStatus;
import com.mygdx.labyrinth.entity.component.collisions.DynamicBody;
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
        HitBox hitBox = new HitBox(startXPosition, startYPosition, 0.9f, 1.2f, true, true);
        entity.addComponent(hitBox);
        entity.addComponent(new Direction(0, 0));
        entity.addComponent(new Velocity(0.1f));
        entity.addComponent(new SoundPlayer(Resource.HERO_WALK_SOUND, Constante.HERO_WALK_DELTASOUND));
        entity.addComponent(new DynamicBody());
        entity.addComponent(CollisionStatus.NONE);
        Vie vie = new Vie(6);
        entity.addComponent(vie);
        Argent arg = new Argent(0);
        entity.addComponent(arg);

        CollisionHandler collisionHandler = (e1, e2) -> {

            if (e2.getGroupName().equals("coins")) {
                arg.setArgent(arg.getArgent() + 1);
                e2.getComponent(SoundPlayer.class).getSound().play(0.2f);
                e2.addComponent(CollisionStatus.MARK_AS_REMOVE);
            } else if (e2.getGroupName().equals("potions")) {
                vie.setVie(6);
                e2.addComponent(CollisionStatus.MARK_AS_REMOVE);
            }

            if ("walls".equals(e2.getGroupName())) {
                hitBox.setY(hitBox.getOldPosition().y);
                hitBox.setX(hitBox.getOldPosition().x);
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
        HitBox hitBox = new HitBox(startXPosition, startYPosition, 0.9f, 1f, true, true);
        entity.addComponent(hitBox);
        entity.addComponent(new Direction(0, 0));
        entity.addComponent(new Velocity(0.05f));
        entity.addComponent(new Vision(
                startXPosition ,
                startYPosition,
                10f));
        entity.addComponent(new DynamicBody());
        entity.addComponent(CollisionStatus.NONE);
        entity.addComponent(new SoundPlayer(Resource.MONSTER));
        Vie vie = new Vie(3);
        entity.addComponent(vie);
        TimerManager timers = new TimerManager();
        timers.createTimer("wait");
        timers.createTimer("move");
        timers.setActif("move", true);
        entity.addComponent(timers);
        entity.addComponent(new Position(0,0));

        CollisionHandler collisionHandler = (e1, e2) -> {
            if (e2.getGroupName().equals("heroArrows")) {
                e2.addComponent(CollisionStatus.MARK_AS_REMOVE);
                vie.setVie(vie.getVie() - 1);
            }
            if (e2.getGroupName().equals("walls")) {
                hitBox.getBox().setPosition(hitBox.getX(), hitBox.getY());
            }
            if (e2.getName().equals("hero")) {
                if (!timers.isActif("wait")) {
                    Vie vieHero = e2.getComponent(Vie.class);
                    vieHero.setVie(vieHero.getVie() - 1);
                    timers.resetOf("wait");
                    timers.setActif("wait", true);
                }
               }

            if ("walls".equals(e2.getGroupName())) {
                hitBox.setY(hitBox.getOldPosition().y);
                hitBox.setX(hitBox.getOldPosition().x);
            }
            if (e2.getGroupName().equals("walls")) {
                hitBox.setY(hitBox.getOldPosition().y);
                hitBox.setX(hitBox.getOldPosition().x);
            }
        };

        entity.setCollisionHandler(collisionHandler);

        return entity;
    }

    public static Entity createLittle(String name, float startXPosition, float startYPosition) {
        Entity entity = new Entity(name, "enemies");
        Map<String, AnimatedSpriteList.AnimationData> animations = Map.of(
                "idle", new AnimatedSpriteList.AnimationData(0.15f, 0, 4),
                "run", new AnimatedSpriteList.AnimationData(0.105f, 4, 8)
        );
        entity.addComponent(new AnimatedSpriteList(Resource.LITTLE_TEXTURE, 8, 1, animations));
        HitBox hitBox = new HitBox(startXPosition, startYPosition, 0.9f, 1f, true, true);
        entity.addComponent(hitBox);
        entity.addComponent(new Direction(0, 0));
        entity.addComponent(new Velocity(0.09f));
        entity.addComponent(new Vision(
                startXPosition ,
                startYPosition,
                8f));
        entity.addComponent(new DynamicBody());
        entity.addComponent(CollisionStatus.NONE);
        entity.addComponent(new SoundPlayer(Resource.MONSTER));
        Vie vie = new Vie(1);
        entity.addComponent(vie);
        TimerManager timers = new TimerManager();
        timers.createTimer("wait");
        timers.createTimer("move");
        timers.setActif("move", true);
        entity.addComponent(timers);
        entity.addComponent(new Position(0,0));

        CollisionHandler collisionHandler = (e1, e2) -> {
            if (e2.getGroupName().equals("heroArrows")) {
                e2.addComponent(CollisionStatus.MARK_AS_REMOVE);
                vie.setVie(vie.getVie() - 1);
            }
            if (e2.getGroupName().equals("walls")) {
                hitBox.getBox().setPosition(hitBox.getX(), hitBox.getY());
            }

            if (e2.getName().equals("hero")) {
                if (!timers.isActif("wait")) {
                    Vie vieHero = e2.getComponent(Vie.class);
                    vieHero.setVie(vieHero.getVie() - 1);
                    timers.resetOf("wait");
                    timers.setActif("wait", true);
                }
            }

            if ("walls".equals(e2.getGroupName())) {
                hitBox.setY(hitBox.getOldPosition().y);
                hitBox.setX(hitBox.getOldPosition().x);
            }
        };

        entity.setCollisionHandler(collisionHandler);

        return entity;
    }

    public static Entity createBigZombie(String name, float startXPosition, float startYPosition) {
        Entity entity = new Entity(name, "enemies");
        Map<String, AnimatedSpriteList.AnimationData> animations = Map.of(
                "idle", new AnimatedSpriteList.AnimationData(0.15f, 0, 4),
                "run", new AnimatedSpriteList.AnimationData(0.105f, 4, 8)
        );
        entity.addComponent(new AnimatedSpriteList(Resource.BIGZOMBIE_TEXTURE, 8, 1, animations));
        HitBox hitBox = new HitBox(startXPosition, startYPosition, 1.3f, 1.5f, true, true);
        entity.addComponent(hitBox);
        entity.addComponent(new Direction(0, 0));
        entity.addComponent(new Velocity(0.101f));
        entity.addComponent(new Vision(
                startXPosition,
                startYPosition,
                20f));
        entity.addComponent(new DynamicBody());
        entity.addComponent(CollisionStatus.NONE);
        entity.addComponent(new SoundPlayer(Resource.BIG_MONSTER));
        Vie vie = new Vie(5);
        entity.addComponent(vie);
        TimerManager timers = new TimerManager();
        timers.createTimer("wait");
        entity.addComponent(timers);

        CollisionHandler collisionHandler = (e1, e2) -> {
            if (e2.getGroupName().equals("heroArrows")) {
                e2.addComponent(CollisionStatus.MARK_AS_REMOVE);
                vie.setVie(vie.getVie() - 1);
            }
            if (e2.getGroupName().equals("walls")) {
                hitBox.getBox().setPosition(hitBox.getX(), hitBox.getY());
            }
            if (e2.getName().equals("hero")) {
                if (!timers.isActif("wait")) {
                    Vie vieHero = e2.getComponent(Vie.class);
                    vieHero.setVie(vieHero.getVie() - 2);
                    timers.resetOf("wait");
                    timers.setActif("wait", true);
                }
            }

            if ("walls".equals(e2.getGroupName())) {
                hitBox.setY(hitBox.getOldPosition().y);
                hitBox.setX(hitBox.getOldPosition().x);
            }
        };

        entity.setCollisionHandler(collisionHandler);

        return entity;
    }

    public static Entity createBigDevil(String name, float startXPosition, float startYPosition) {
        Entity entity = new Entity(name, "enemies");
        Map<String, AnimatedSpriteList.AnimationData> animations = Map.of(
                "idle", new AnimatedSpriteList.AnimationData(0.15f, 0, 4),
                "run", new AnimatedSpriteList.AnimationData(0.105f, 4, 8)
        );
        entity.addComponent(new AnimatedSpriteList(Resource.BIGDEVIL_TEXTURE, 8, 1, animations));
        HitBox hitBox = new HitBox(startXPosition, startYPosition, 1.3f, 1.5f, true, true);
        entity.addComponent(hitBox);
        entity.addComponent(new Direction(0, 0));
        entity.addComponent(new Velocity(0.05f));
        entity.addComponent(new Vision(
                startXPosition,
                startYPosition,
                50f));
        entity.addComponent(new DynamicBody());
        entity.addComponent(CollisionStatus.NONE);
        Vie vie = new Vie(20);
        entity.addComponent(vie);
        entity.addComponent(new SoundPlayer(Resource.BIG_MONSTER));
        TimerManager timers = new TimerManager();
        timers.createTimer("wait");
        entity.addComponent(timers);

        CollisionHandler collisionHandler = (e1, e2) -> {
            if (e2.getGroupName().equals("heroArrows")) {
                e2.addComponent(CollisionStatus.MARK_AS_REMOVE);
                vie.setVie(vie.getVie() - 1);
            }
            if (e2.getGroupName().equals("walls")) {
                hitBox.getBox().setPosition(hitBox.getX(), hitBox.getY());
            }
            if (e2.getName().equals("hero")) {
                if (!timers.isActif("wait")) {
                    Vie vieHero = e2.getComponent(Vie.class);
                    vieHero.setVie(vieHero.getVie() - Constante.VIE_HERO_MAX);
                    timers.resetOf("wait");
                    timers.setActif("wait", true);
                }
            }

            if ("walls".equals(e2.getGroupName())) {
                hitBox.setY(hitBox.getOldPosition().y);
                hitBox.setX(hitBox.getOldPosition().x);
            }
        };

        entity.setCollisionHandler(collisionHandler);

        return entity;
    }

    public static Entity createMage(String name, float startXPosition, float startYPosition) {
        Entity entity = new Entity(name, "enemies");
        Map<String, AnimatedSpriteList.AnimationData> animations = Map.of(
                "idle", new AnimatedSpriteList.AnimationData(0.15f, 1, 3),
                "run", new AnimatedSpriteList.AnimationData(0.105f, 0, 4)
        );
        entity.addComponent(new AnimatedSpriteList(Resource.MAGE_TEXTURE, 4, 1, animations));
        HitBox hitBox = new HitBox(startXPosition, startYPosition, 1.3f, 1.5f, true, true);
        entity.addComponent(hitBox);
        entity.addComponent(new Direction(0, 0));
        entity.addComponent(new Velocity(0.1f));
        entity.addComponent(new Vision(
                startXPosition,
                startYPosition,
                5f));
        entity.addComponent(new DynamicBody());
        entity.addComponent(CollisionStatus.NONE);
        Vie vie = new Vie(2);
        entity.addComponent(vie);
        TimerManager timers = new TimerManager();
        timers.createTimer("wait");
        entity.addComponent(timers);

        CollisionHandler collisionHandler = (e1, e2) -> {
            if (e2.getGroupName().equals("heroArrows")) {
                e2.addComponent(CollisionStatus.MARK_AS_REMOVE);
                vie.setVie(vie.getVie() - 1);
            }
            if (e2.getGroupName().equals("walls")) {
                hitBox.getBox().setPosition(hitBox.getX(), hitBox.getY());
            }
            if (e2.getName().equals("hero")) {
                if (!timers.isActif("wait")) {
                    Vie vieHero = e2.getComponent(Vie.class);
                    vieHero.setVie(vieHero.getVie() - 2);
                    timers.resetOf("wait");
                    timers.setActif("wait", true);
                }
            }

            if ("walls".equals(e2.getGroupName())) {
                hitBox.setY(hitBox.getOldPosition().y);
                hitBox.setX(hitBox.getOldPosition().x);
            }
        };

        entity.setCollisionHandler(collisionHandler);

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

    public static Entity createStaff(String name, float positionX, float positionY, float width, float height) {
        Entity entity = new Entity(name);

        Map<String, AnimatedSpriteList.AnimationData> animations = Map.of(
                "idle", new AnimatedSpriteList.AnimationData(0.105f, 0, 1)
        );

        entity.addComponent(new HitBox(positionX, positionY, width, height, false, false));
        entity.addComponent(new AnimatedSpriteList(Resource.STAFF_TEXTURE, 1, 1, animations));
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
        entity.addComponent(new Velocity(3f));
        entity.addComponent(new DynamicBody());
        Trajectory tj = new Trajectory(direction , ARROW_DISTANCE);
        entity.addComponent(tj);
        entity.addComponent(CollisionStatus.NONE);
        hb.getBox().setRotation(tj.getAngle() - 90f);

        CollisionHandler collisionHandler = (e1, e2) -> {
            if (e2.getGroupName().equals("enemies")) {
                entity.addComponent(CollisionStatus.MARK_AS_REMOVE);
//                Vie vie = e2.getComponent(Vie.class);
//                vie.setVie(vie.getVie() - 1);
            }
            if (e2.getGroupName().equals("walls")) {
                entity.addComponent(CollisionStatus.MARK_AS_REMOVE);
            }

            if (e2.getGroupName().equals("coffres")) {
                e2.getComponent(SoundPlayer.class).getSound().play();
                entity.addComponent(CollisionStatus.MARK_AS_REMOVE);
                Vie vieCoffre = e2.getComponent(Vie.class);
                vieCoffre.setVie(0);
            }
        };

        entity.setCollisionHandler(collisionHandler);
        return entity;

    }

    public static Entity createMagic(String name,
                                     String groupName,
                                     float positionX,
                                     float positionY,
                                     float width,
                                     float height,
                                     Vector2 direction) {
        Entity entity = new Entity(name, groupName);
        HitBox hb = new HitBox(positionX, positionY, width, height, true, true);
        entity.addComponent(hb);
        entity.addComponent(new StaticSprite(Resource.MAGIC_TEXTURE));
        entity.addComponent(new Velocity(2f));
        entity.addComponent(new DynamicBody());
        Trajectory tj = new Trajectory(direction , ARROW_DISTANCE);
        entity.addComponent(tj);
        entity.addComponent(CollisionStatus.NONE);
        hb.getBox().setRotation(tj.getAngle() - 90f);

        CollisionHandler collisionHandler = (e1, e2) -> {
            if (e2.getName().equals("hero")) {
                entity.addComponent(CollisionStatus.MARK_AS_REMOVE);
//                Vie vie = e2.getComponent(Vie.class);
//                vie.setVie(vie.getVie() - 1);
            }
            if (e2.getGroupName().equals("walls")) {
                entity.addComponent(CollisionStatus.MARK_AS_REMOVE);
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

    public static Entity createHealPotion(String name, Vector2 pos, float width, float height) {
        Entity entity = new Entity(name, "potions");

        entity.addComponent(new HitBox(pos.x, pos.y, width, height, true, false));
        entity.addComponent(new StaticSprite(Resource.POPO_HEAL));

        return entity;
    }

    public static Entity createChest(String name, Vector2 pos, float width, float height) {
        Entity entity = new Entity(name, "coffres");

        Map<String, AnimatedSpriteList.AnimationData> animations = Map.of(
                "close", new AnimatedSpriteList.AnimationData(0.15f, 0, 1),
                "opening", new AnimatedSpriteList.AnimationData(0.1f, 1, 3)
        );

        entity.addComponent(new HitBox(pos.x, pos.y, width, height, true, false));
        AnimatedSpriteList animatedSpriteList = new AnimatedSpriteList(Resource.ANIMATION_COFFRE, 3, 1, animations);
        animatedSpriteList.setCurrentAnimationName("close");
        entity.addComponent(animatedSpriteList);
        entity.addComponent(new Vie(1));
        entity.addComponent(new SoundPlayer(Resource.CHEST));
        TimerManager timerManager =new TimerManager();
        timerManager.createTimer("destruction");
        entity.addComponent(timerManager);

        return entity;
    }
}
