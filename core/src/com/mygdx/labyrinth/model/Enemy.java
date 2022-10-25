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
import com.mygdx.labyrinth.model.collision.Body;
import com.mygdx.labyrinth.model.collision.BodyType;

public class Enemy implements Entity{

    private Sprite sprite;

    private Texture texture;

    private final Body body;

    private final TextureRegion[][] textures;

    private final Animation<TextureRegion> animationRun;

    private final Animation<TextureRegion> animationIdle;

    private Vector2 position;

    private Vector2 velocite;

    private float width;

    private float height;

    private float stateTime;

    private TiledMapTileLayer collisionLayer;

    private enum Direction {
        RIGTH, LEFT
    }

    private Direction direction;


    public Enemy(float x, float y, float width, float height){
        this.body = new Body(new Rectangle(x, y, width, height), BodyType.ENEMY, this, true);
        this.position = new Vector2(x, y);
        this.width = width;
        this.height = height;
        //this.collisionLayer = collisionLayer;
        this.direction = Direction.RIGTH;

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
        this.move();
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
        this.sprite.setPosition(position.x, position.y);
        this.sprite.draw(batch);


    }

    @Override
    public void dispose() {
        this.texture.dispose();
    }

    @Override
    public Body getBody() {
        return this.body;
    }

    public void setposition() {
        body.getBounds().setPosition(this.position.x, this.position.y);
    }

    @Override
    public void handleCollision(Body b) {
        if(b.getBodyType().equals(BodyType.WALL)) {
            setposition();
        }

    }

    @Override
    public boolean isDestroyed() {
        return false;
    }

    public void move() {
        // Si collision avec un bloc alors aller dans le sens opposé
        velocite.x = -0.09f;
        position.x += velocite.x;
    }
}
