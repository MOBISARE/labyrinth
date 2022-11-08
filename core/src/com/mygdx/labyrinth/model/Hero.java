package com.mygdx.labyrinth.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.labyrinth.exception.LabyrinthException;
import com.mygdx.labyrinth.model.collision.Body;
import com.mygdx.labyrinth.model.collision.BodyType;

public class Hero extends Observable implements Entity {

    //region Attributs

    // Sprite qui contient l'image + la forme du hero
    private final Sprite sprite;

    // Texture du héros complète
    private final Texture imgAnimHero;
    private final Animation<TextureRegion> animationRun;
    // Animation de la marche vers la gauche
    private final Animation<TextureRegion> animationIdle;
    // Vélocité du héros
    private final Vector2 speedVector;
    // Flag marche vers la gauche
    private boolean isMovingLeft;
    // Flag marche vers la droite
    private boolean isMovingRight;
    // Flag marche vers le haut
    private boolean isMovingUp;
    // Flag marche vers le bas
    private boolean isMovingDown;
    // Deplacement u/frame;
    private final float SPEED = 5f;
    //Son de déplacement Hero
    private Sound sound;
    private int deltaSound = 0;

    private float stateTime;

    private int healthPoint;
    private int gold;
    private enum Direction {
        RIGTH, LEFT
    }
    private Direction direction;
    private final Body body;

    private final Vector2 oldPosition;

    //endregion


    //region Constructors

    /**
     * Construit un héros à la position x, y et de taille width, height
     * @param x float
     * @param y float
     * @param width float
     * @param height float
     */
    public Hero(float x, float y, float width, float height) {
        super();
        this.stateTime = 0;
        this.body = new Body(new Rectangle(x, y, width, height), BodyType.HERO, this, true);
        this.oldPosition = new Vector2(x, y);
        this.isMovingLeft = false;
        this.isMovingRight = false;
        this.isMovingUp = false;
        this.isMovingDown = false;
        this.direction = Direction.RIGTH;

        // Création du personnage à l'arrêt
        this.speedVector = new Vector2(0f,0f);

        sound = Gdx.audio.newSound(Gdx.files.internal("sound/sfx_step_grass_l.mp3"));

        imgAnimHero = new Texture(Gdx.files.internal("textures/animation_hero_knight.png"));
        // Animation de la marche vers la droite
        TextureRegion[][] texturesHero = TextureRegion.split(imgAnimHero,
                imgAnimHero.getWidth() / 9,
                imgAnimHero.getHeight());

        TextureRegion[] idle = {texturesHero[0][1], texturesHero[0][2], texturesHero[0][3], texturesHero[0][4]};
        animationIdle = new Animation<>(0.15f, idle );
        TextureRegion[] run = {texturesHero[0][5], texturesHero[0][6], texturesHero[0][7], texturesHero[0][8]};
        animationRun = new Animation<>(0.105f, run );
        this.sprite = new Sprite(animationIdle.getKeyFrame(0, true));
        this.healthPoint = 6;
        this.gold = 0;
    }

    //endregion

    //region Getter & Setter

    @Override
    public Body getBody() {
        return body;
    }

    /**+
     * Position le flag pour le déplacement vers la gauche
     * @param t boolean
     */
    public void setMovingLeft(boolean t) {
        if (isMovingRight && t) isMovingRight = false;
        isMovingLeft = t;
        direction = Direction.LEFT;
    }

    /**+
     * Position le flag pour le déplacement vers la droite
     * @param t boolean
     */
    public void setMovingRight(boolean t) {
        if (isMovingLeft && t) isMovingLeft = false;
        isMovingRight = t;
        direction = Direction.RIGTH;
    }

    /**+
     * Position le flag pour le déplacement vers le haut
     * @param t boolean
     */
    public void setMovingUp(boolean t) {
        if (isMovingDown && t) isMovingDown = false;
        isMovingUp = t;
    }

    /**+
     * Position le flag pour le déplacement vers le bas
     * @param t boolean
     */
    public void setMovingDown(boolean t) {
        if (isMovingUp && t) isMovingUp = false;
        isMovingDown = t;
    }

    public Vector2 getPosition() {
        Vector2 v2 = new Vector2();
        body.getBounds().getPosition(v2);
        return v2;
    }

    public float getPositionX() {
        return body.getBounds().getX();
    }

    public float getPositionY() {
        return body.getBounds().getY();
    }

    public void setHealthPoint(int healthPoint) {
        this.healthPoint = healthPoint;
        if (this.healthPoint < 0) {
            this.healthPoint = 0;
        }
        try {
            notifierObservers("vie_hero_event", this);
        } catch (LabyrinthException e) {
            throw new RuntimeException(e);
        }
    }

    public int getHealthPoint() {
        return healthPoint;
    }

    public int getGold() {
        return this.gold;
    }

    public void addArgent(int n) {
        this.gold += n;
        if (gold < 0) {
            gold = 0;
        }
        try {
            notifierObservers("argent_hero_event", this);
        } catch (LabyrinthException e) {
            throw new RuntimeException(e);
        }
    }

    public void setOldPosition() {
        body.getBounds().setPosition(oldPosition.x, oldPosition.y);
    }

    //endregion


    /**
     * {@inheritDoc}
     * @param batch SpriteBatch, batch utilisé pour le rendu
     * @param deltaTime float, temps écoulé depuis la dernière image affichée
     */
    @Override
    public void render(SpriteBatch batch, float deltaTime) {
        this.stateTime += deltaTime;
        this.updateMotion(deltaTime);

        // Update du sprite à afficher
        if (speedVector.x == 0f && speedVector.y == 0f && direction == Direction.RIGTH) {
            TextureRegion frame = animationIdle.getKeyFrame(stateTime, true);
            if (frame.isFlipX()) {
                frame.flip(true, false);
            }
            sprite.setRegion(frame);
        } else if (speedVector.x == 0f && speedVector.y == 0f && direction == Direction.LEFT) {
            TextureRegion frame = animationIdle.getKeyFrame(stateTime, true);
            if (!frame.isFlipX()) {
                frame.flip(true, false);
            }
            sprite.setRegion(frame);
        } else if (speedVector.x > 0f) {
            TextureRegion frame = animationRun.getKeyFrame(stateTime, true);
            if (frame.isFlipX()) {
                frame.flip(true, false);
            }
            sprite.setRegion(frame);
        } else if (speedVector.x < 0f) {
            TextureRegion frame = animationRun.getKeyFrame(stateTime, true);
            if (!frame.isFlipX()) {
                frame.flip(true, false);
            }
            sprite.setRegion(frame);
        } else if (speedVector.y != 0f && direction == Direction.RIGTH) {
            TextureRegion frame = animationRun.getKeyFrame(stateTime, true);
            if (frame.isFlipX()) {
                frame.flip(true, false);
            }
            sprite.setRegion(frame);
        } else if (speedVector.y != 0f && direction == Direction.LEFT) {
            TextureRegion frame = animationRun.getKeyFrame(stateTime, true);
            if (!frame.isFlipX()) {
                frame.flip(true, false);
            }
            sprite.setRegion(frame);
        }

        sprite.setSize(body.getBounds().width + 0.2f, body.getBounds().height + 0.5f);
        sprite.setPosition(body.getBounds().x - 0.1f, body.getBounds().y);
        sprite.draw(batch);

        if((speedVector.x != 0 || speedVector.y != 0) && deltaSound==0 ) {
            deltaSound=25;
            long id = sound.play(0.2f);
            sound.setPitch(id, 2);
            sound.setLooping(id,false);
        }
        if(deltaSound != 0) deltaSound--;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        imgAnimHero.dispose();
        sound.dispose();
    }

    /**
     * Fonctione intermédiaire pour faire avancer le héro
     * @param delta float
     */
    private void updateMotion(float delta) {

        // On récupère l'ancienne position au cas ou
        oldPosition.set(body.getBounds().x, body.getBounds().y);

        if (isMovingLeft) {
            speedVector.set(-0.09f, speedVector.y);
        }

        if (isMovingRight) {
            speedVector.set(0.09f, speedVector.y);
        }

        if (isMovingUp) {
            speedVector.set(speedVector.x, 0.09f);
        }

        if (isMovingDown) {
            speedVector.set(speedVector.x, -0.09f);
        }

        if (!isMovingRight && !isMovingLeft && !isMovingDown && !isMovingUp) {
            speedVector.set(0f , 0f);
        } else {
            if (!isMovingRight && !isMovingLeft) {
                speedVector.set(0f , speedVector.y);
            } else if (!isMovingUp && !isMovingDown) {
                speedVector.set(speedVector.x, 0f);
            }
        }

        //On effectue le déplacement
        speedVector.nor();
        speedVector.scl(SPEED * Gdx.graphics.getDeltaTime());
        body.getBounds().setPosition(body.getBounds().x + speedVector.x,
                body.getBounds().y + speedVector.y);
    }

    /**
     * {@inheritDoc}
     * @param b Body
     */
    @Override
    public void handleCollision(Body b) {
        switch (b.getBodyType()) {
            case WALL:
                setOldPosition();
                break;
            case COIN:
                addArgent(1);
                b.destroyed();
                b.getEntityParent().dispose();
                break;
            case ENEMY:

                this.setHealthPoint(this.healthPoint-1);
                if (this.oldPosition.x <= b.getBounds().getX()) {
                    this.oldPosition.x -= 0.3f;
                } else {
                    this.oldPosition.x += 0.3f;
                }
                if (this.oldPosition.y <= b.getBounds().getY()) {
                    this.oldPosition.y -= 0.3f;
                } else {
                    this.oldPosition.y += 0.3f;
                }
                if (getHealthPoint() <= 0) {
                    System.out.println("GAME OVER");
                }
                setOldPosition();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean isDestroyed() {
        return false;
    }

    @Override
    public String getName() {
        return "Hero";
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Hero{");
        sb.append("body=").append(body);
        sb.append('}');
        return sb.toString();
    }
}
