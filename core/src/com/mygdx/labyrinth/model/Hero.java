package com.mygdx.labyrinth.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Hero implements Entity {

    // Sprite qui contient l'image + la forme du hero
    private Sprite sprite;

    // Texture du héro complète
    private final Texture imgAnimHero;
    // Animation de la marche vers la droite
    private final TextureRegion[][] texturesHero;
    private final Animation<TextureRegion> animationMarcheD;
    // Animation de la marche vers la gauche
    private final Animation<TextureRegion> animationMarcheG;
    // Animation de la marche vers le haut
    private final Animation<TextureRegion> animationMarcheH;
    // Animation de la marche vers le bas
    private final Animation<TextureRegion> animationMarcheB;
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

    private float stateTime;


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

        // Création du personnage à l'arrêt
        this.velocite = new Vector2(0f,0f);

        imgAnimHero = new Texture(Gdx.files.internal("heros.png"));
        this.texturesHero = TextureRegion.split(imgAnimHero,
                imgAnimHero.getWidth() / 3,
                imgAnimHero.getHeight() / 4);

        animationMarcheH = new Animation<>(0.15f, texturesHero[0]);
        animationMarcheD = new Animation<>(0.15f, texturesHero[1]);
        animationMarcheB = new Animation<>(0.15f, texturesHero[2]);
        animationMarcheG = new Animation<>(0.15f, texturesHero[3]);

        this.sprite = new Sprite(animationMarcheD.getKeyFrame(0, true));
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
        if (velocite.x == 0f && velocite.y == 0f) {
            sprite.setRegion(texturesHero[2][1]);
        } else if (velocite.x > 0) {
            sprite.setRegion(animationMarcheD.getKeyFrame(stateTime, true));
        } else if (velocite.x < 0) {
            sprite.setRegion(animationMarcheG.getKeyFrame(stateTime, true));
        } else if (velocite.y > 0) {
            sprite.setRegion(animationMarcheH.getKeyFrame(stateTime, true));
        } else if (velocite.y < 0) {
            sprite.setRegion(animationMarcheB.getKeyFrame(stateTime, true));
        }

        sprite.setSize(width, height);
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);
    }

    /**
     * Libère les ressources lorsque c'est nécessaire
     */
    @Override
    public void dispose() {
        imgAnimHero.dispose();
    }

    /**+
     * Position le flag pour le déplacement vers la gauche
     * @param t
     */
    public void setLeftMove(boolean t) {
        if (rightMove && t) rightMove = false;
        leftMove = t;
    }

    /**+
     * Position le flag pour le déplacement vers la droite
     * @param t
     */
    public void setRightMove(boolean t) {
        if (leftMove && t) leftMove = false;
        rightMove = t;
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
            //On vérifie si le déplacement est autorisé
            leftTiledBlocked = collisionLayer.getCell((int)(position.x - 1), (int)(position.y)).getTile().getProperties().containsKey("blocked");
        }

        if (rightMove) {
            velocite.set(0.09f, velocite.y);
            //On vérifie si le déplacement est autorisé
            rightTiledBlocked = collisionLayer.getCell((int)(position.x + 1), (int)(position.y)).getTile().getProperties().containsKey("blocked");
        }

        if (upMove) {
            velocite.set(velocite.x, 0.09f);
            //On vérifie si le déplacement est autorisé
            upTiledBlocked = collisionLayer.getCell((int)position.x, (int)(position.y + height)).getTile().getProperties().containsKey("blocked");
        }

        if (downMove) {
            velocite.set(velocite.x, -0.09f);
            //On vérifie si le déplacement est autorisé
            downTiledBlocked = collisionLayer.getCell((int)position.x, (int)position.y-1).getTile().getProperties().containsKey("blocked");
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
}
