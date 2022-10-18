package com.mygdx.labyrinth.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.labyrinth.game.level.Level0;

public class Hero implements Entity {

    // Sprite qui contient l'image + la forme du hero
    private Sprite sprite;

    // Texture du héro complète
    private final Texture imgAnimHero;
    // Animation de la marche vers la droite
    private final TextureRegion[][] texturesHero;
    private final Animation<TextureRegion> animationRun;
    // Animation de la marche vers la gauche
    private final Animation<TextureRegion> animationIdle;
    private Vector2 position;
    // Largeur du héro
    private float width;
    // Hauteur du héro
    private float height;
    // Vélocité du héros
    private Vector2 velocite;
    // Flag marche vers la gauche
    private boolean leftMove;
    // Flag marche vers la droite
    private boolean rightMove;
    // Flag marche vers le haut
    private boolean upMove;
    // Flag marche vers le bas
    private boolean downMove;
    // Deplacement u/frame;
    private float vitesse = 5f;
    //TiledMap pour les collisions
    private TiledMapTileLayer collisionLayer;
    //Son de déplacement Hero
    private Sound sound;
    private int deltaSound = 0;

    private float stateTime;

    private int vie = 6;
    private int argent = 0;
    private enum Direction {
        RIGTH, LEFT
    }
    private Direction direction;

    /**
     * Construit un héros à la position x, y et de taille width, height
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public Hero(float x, float y, float width, float height, TiledMapTileLayer collisionLayer) {
        this.stateTime = 0;
        this.position = new Vector2(x, y);
        this.width = width;
        this.height = height;
        this.leftMove = false;
        this.rightMove = false;
        this.upMove = false;
        this.downMove = false;
        this.collisionLayer = collisionLayer;
        this.direction = Direction.RIGTH;

        // Création du personnage à l'arrêt
        this.velocite = new Vector2(0f,0f);

        sound = Gdx.audio.newSound(Gdx.files.internal("sound/sfx_step_grass_l.mp3"));
        imgAnimHero = new Texture(Gdx.files.internal("textures/animation_hero_knight.png"));
        this.texturesHero = TextureRegion.split(imgAnimHero,
                imgAnimHero.getWidth() / 9,
                imgAnimHero.getHeight());

        TextureRegion[] idle = {texturesHero[0][1], texturesHero[0][2], texturesHero[0][3], texturesHero[0][4]};
        animationIdle = new Animation<>(0.15f, idle );
        TextureRegion[] run = {texturesHero[0][5], texturesHero[0][6], texturesHero[0][7], texturesHero[0][8]};
        animationRun = new Animation<>(0.10f, run );
        this.sprite = new Sprite(animationIdle.getKeyFrame(0, true));
    }

    /**
     * Permet de dessiner le héros dans la fenêtre
     * @param deltaTime
     */
    @Override
    public void render(SpriteBatch batch, float deltaTime) {
        this.stateTime += deltaTime;
        this.updateMotion(deltaTime);

        // Update du sprite à afficher
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

        sprite.setSize(width, height);
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);

        if((velocite.x != 0 || velocite.y != 0) && deltaSound==0 ) {
            deltaSound=25;
            long id = sound.play(0.1f);
            sound.setPitch(id, 2);
            sound.setLooping(id,false);
        }
        if(deltaSound != 0) deltaSound--;
    }

    /**
     * Libère les ressources lorsque c'est nécessaire
     */
    @Override
    public void dispose() {
        imgAnimHero.dispose();
        sound.dispose();
    }

    /**+
     * Position le flag pour le déplacement vers la gauche
     * @param t
     */
    public void setLeftMove(boolean t) {
        if (rightMove && t) rightMove = false;
        leftMove = t;
        direction = Direction.LEFT;
    }

    /**+
     * Position le flag pour le déplacement vers la droite
     * @param t
     */
    public void setRightMove(boolean t) {
        if (leftMove && t) leftMove = false;
        rightMove = t;
        direction = Direction.RIGTH;
    }

    /**+
     * Position le flag pour le déplacement vers le haut
     * @param t
     */
    public void setUpMove(boolean t) {
        if (downMove && t) downMove = false;
        upMove = t;
    }

    /**+
     * Position le flag pour le déplacement vers le bas
     * @param t
     */
    public void setDownMove(boolean t) {
        if (upMove && t) upMove = false;
        downMove = t;
    }

    /**
     * Fonctione intermédiaire pour faire avancer le héro
     * @param delta
     */
    private void updateMotion(float delta) {
        //Variables pour la gestion des collisions
        boolean leftTiledBlocked = false, rightTiledBlocked = false, upTiledBlocked = false, downTiledBlocked = false;

        //On stock les anciennes positions
        float oldX = getPosition().x;
        float oldY = getPosition().y;

        if (leftMove) {
            velocite.set(-0.09f, velocite.y);
            //On vérifie le déplacement
            leftTiledBlocked = collisionLayer.getCell((int)(position.x - 0.09), (int)(position.y)).getTile().getProperties().containsKey("blocked")
                            || collisionLayer.getCell((int)(position.x - 0.09), (int)(position.y + (int)Math.floor(height))).getTile().getProperties().containsKey("blocked");
        }

        if (rightMove) {
            velocite.set(0.09f, velocite.y);
            //On vérifie le déplacement
            rightTiledBlocked = collisionLayer.getCell((int)(position.x + width + 0.09), (int)(position.y)).getTile().getProperties().containsKey("blocked")
                             || collisionLayer.getCell((int)(position.x + width + 0.09), (int)(position.y + (int)Math.floor(height))).getTile().getProperties().containsKey("blocked");
        }

        if (upMove) {
            velocite.set(velocite.x, 0.09f);
            //On vérifie le déplacement
            upTiledBlocked = collisionLayer.getCell((int)position.x, (int)(position.y + height + 0.09)).getTile().getProperties().containsKey("blocked")
                          || collisionLayer.getCell((int)(position.x + Math.ceil(width)), (int)(position.y + height + 0.09)).getTile().getProperties().containsKey("blocked");
        }

        if (downMove) {
            velocite.set(velocite.x, -0.09f);
            //On vérifie le déplacement est autorisé
            downTiledBlocked = collisionLayer.getCell((int)position.x, (int)(position.y - 0.09)).getTile().getProperties().containsKey("blocked")
                            || collisionLayer.getCell((int)(position.x + Math.ceil(width)), (int)(position.y - 0.09)).getTile().getProperties().containsKey("blocked");
        }

        if (!rightMove && !leftMove && !downMove && !upMove) {
            velocite.set(0f , 0f);
        } else {
            if (!rightMove && !leftMove) {
                velocite.set(0f , velocite.y);
            } else if (!upMove && !downMove) {
                velocite.set(velocite.x, 0f);
            }
        }

        //On effectue le déplacement
        velocite.nor();
        velocite.scl(vitesse * Gdx.graphics.getDeltaTime());
        position.add(velocite);

        //On rectifie si déplacement interdit
        if(leftTiledBlocked || rightTiledBlocked || upTiledBlocked || downTiledBlocked) {
            position.x = oldX;
            position.y = oldY;

            if(leftTiledBlocked) leftMove = false;
            if(rightTiledBlocked) rightMove = false;
            if(upTiledBlocked) upMove = false;
            if(downTiledBlocked) downMove = false;
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setVie(int vie) {
        this.vie = vie;
    }

    public int getVie() {
        return vie;
    }

    public int getArgent() {
        return this.argent;
    }

    public void addArgent(int n) {
        this.argent += n;
    }
}
