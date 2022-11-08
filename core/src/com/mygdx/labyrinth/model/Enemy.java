package com.mygdx.labyrinth.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.mygdx.labyrinth.model.collision.Body;
import com.mygdx.labyrinth.model.collision.BodyType;
import com.mygdx.labyrinth.model.event.Event;
import com.mygdx.labyrinth.model.event.TimeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Enemy implements Entity{

    private Sprite sprite;

    private Texture texture;

    private final Body body;

    private final TextureRegion[][] textures;

    private final Animation<TextureRegion> animationRun;

    private final Animation<TextureRegion> animationIdle;

    private Vector2 oldPosition;

    private Vector2 velocite;

    private float width;

    private float height;

    private float stateTime;

    private TiledMapTileLayer collisionLayer;

    private enum Direction {
        RIGTH, LEFT
    }

    private Direction direction;

    private final String name;

    private final List<Event> events;

    private boolean waiting;


    public Enemy(String name, float x, float y, float width, float height){
        this.name = name;
        this.body = new Body(new Rectangle(x, y, width, height), BodyType.ENEMY, this, true);
        this.oldPosition = new Vector2(x, y);
        this.width = width;
        this.height = height;
        //this.collisionLayer = collisionLayer;
        this.direction = Direction.RIGTH;
        this.events = new ArrayList<>();
        Event timeEvent = new TimeEvent(dt -> this.setWaiting(false), 3000);
        this.events.add(timeEvent);

        // Création du personnage à l'arrêt
        this.velocite = new Vector2(0f,0f);

        this.texture = new Texture(Gdx.files.internal("textures/animation_maskull.png"));
        this.textures = TextureRegion.split(texture,
                texture.getWidth() / 8,
                texture.getHeight());

        TextureRegion[] idle = {textures[0][0], textures[0][1], textures[0][2], textures[0][3]};
        animationIdle = new Animation<>(0.15f, idle );
        TextureRegion[] run = {textures[0][4], textures[0][5], textures[0][6], textures[0][7]};
        animationRun = new Animation<>(0.10f, run );
        this.sprite = new Sprite(animationIdle.getKeyFrame(0, true));
    }

    @Override
    public void render(SpriteBatch batch, float deltaTime) {
        this.stateTime += deltaTime;
        this.events.forEach(Event::update);
        if(!this.waiting){
            this.move();
        }
        if (velocite.x == 0f && velocite.y == 0f && direction == Direction.RIGTH) {
            TextureRegion frame = animationIdle.getKeyFrame(stateTime, true);
            if (frame.isFlipX()) {
                frame.flip(true, false);
            }
            sprite.setRegion(frame);
        } else if (velocite.x == 0f && velocite.y == 0f && direction == Direction.LEFT) {
            TextureRegion frame = animationIdle.getKeyFrame(stateTime, true);
            if (!frame.isFlipX()) {
                frame.flip(true, false);
            }
            sprite.setRegion(frame);
        } else if (velocite.x > 0f) {
            TextureRegion frame = animationRun.getKeyFrame(stateTime, true);
            if (frame.isFlipX()) {
                frame.flip(true, false);
            }
            sprite.setRegion(frame);
        } else if (velocite.x < 0f) {
            TextureRegion frame = animationRun.getKeyFrame(stateTime, true);
            if (!frame.isFlipX()) {
                frame.flip(true, false);
            }
            sprite.setRegion(frame);
        } else if (velocite.y != 0f && direction == Direction.RIGTH) {
            TextureRegion frame = animationRun.getKeyFrame(stateTime, true);
            if (frame.isFlipX()) {
                frame.flip(true, false);
            }
            sprite.setRegion(frame);
        } else if (velocite.y != 0f && direction == Direction.LEFT) {
            TextureRegion frame = animationRun.getKeyFrame(stateTime, true);
            if (!frame.isFlipX()) {
                frame.flip(true, false);
            }
            sprite.setRegion(frame);
        }

        this.sprite.setSize(width, height);
        this.sprite.setPosition(oldPosition.x, oldPosition.y);
        this.sprite.draw(batch);

        // On réinitialise tous les événements de type TimeEvent
        this.events
                .stream()
                .filter(e -> e instanceof TimeEvent)
                .filter(Event::isFinished)
                .forEach(Event::reset);
    }

    @Override
    public void dispose() {
        this.texture.dispose();
    }

    @Override
    public Body getBody() {
        return this.body;
    }

    public void setOldPosition() {
        body.getBounds().setPosition(this.oldPosition.x, this.oldPosition.y);
    }

    @Override
    public void handleCollision(Body b) {
        //System.out.println("Waiting: " + waiting);
        switch (b.getBodyType()) {
            case WALL:
                stop();
                velocite.x = 0.05f;
                oldPosition.x += velocite.x;
                break;
            case HERO:
                this.stop();
                this.waiting = true;
               /* if (b.getBounds().getX() <= this.oldPosition.x) {
                    this.oldPosition.x += 0.2f;
                } else {
                    this.oldPosition.x -= 0.2f;
                }
                if (b.getBounds().getY() <= this.oldPosition.y) {
                    this.oldPosition.y -= 0.2f;
                } else {
                    this.oldPosition.y += 0.2f;
                }*/
                setOldPosition();
                break;
            default:
                //move();
                break;
        }
    }

    @Override
    public boolean isDestroyed() {
        return false;
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    @Override
    public String getName() {
        return this.name;
    }

    private void move() {
        Hero hero = (Hero) EntityManager.getInstance().findByName("Hero")
                .orElseThrow(() -> new IllegalStateException("Erreur : Héro non trouvé"));

        if (oldPosition.x < hero.getPositionX()) {
            direction = Direction.RIGTH;
            velocite.x = 0.05f;
            oldPosition.x += velocite.x;
        } else {
            direction = Direction.LEFT;
            velocite.x = -0.05f;
            oldPosition.x += velocite.x;
        }
        if (oldPosition.y < hero.getPositionY()) {
            velocite.y = 0.05f;
            oldPosition.y += velocite.y;
        } else {
            velocite.y = 0.05f;
            oldPosition.y -= velocite.y;
        }
        setOldPosition();
    }

    public void stop() {
        velocite.x = 0;
        velocite.y = 0;
        setOldPosition();
    }

/*
    public void move() {
        // On récupère l'ancienne position au cas ou
        //oldPosition.set(body.getBounds().x, body.getBounds().y);
        Random random = new Random();
        int timer = random.nextInt(5) + 1; //Intervalle [1-5] inclus
        boolean isMovingRight = random.nextBoolean();
        boolean isMovingUp = random.nextBoolean();
        float time = 0;
        while (time <= timer) {
            if (isMovingRight) {
                this.velocite.x = 0.05f;
            } else {
                this.velocite.x = -0.05f;
            }
            this.oldPosition.x += this.velocite.x;
            if (isMovingUp) {
                this.velocite.y = 0.05f;
            } else {
                this.velocite.y = -0.05f;
            }
            this.oldPosition.y += this.velocite.y;
            setOldPosition();
            time += 0.0001f;
        }
        this.velocite.x = 0;
        this.velocite.y = 0;
        //setOldPosition();


        // Si collision avec un bloc alors aller dans le sens opposé

    }

 */

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Enemy{");
        sb.append("body=").append(body);
        sb.append('}');
        return sb.toString();
    }
}
