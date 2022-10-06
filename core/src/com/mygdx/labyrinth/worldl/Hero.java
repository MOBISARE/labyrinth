package com.mygdx.labyrinth.worldl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Hero implements WorldObject {

    // Sprite qui contient l'image + la forme du hero
    private Sprite hero;
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
    private float vitesse = 0.09f;


    /**
     * Construit un héros à la position x, y et de taille width, height
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public Hero(float x, float y, float width, float height) {
        this.position = new Vector2(x, y);
        this.width = width;
        this.height = height;
        this.leftMove = false;
        this.rightMove = false;
        this.upMove = false;
        this.downMove = false;

        // Création du personnage à l'arrêt
        this.velocite = new Vector2(0f,0f);

        imgAnimHero = new Texture(Gdx.files.internal("heros.png"));
        this.texturesHero = TextureRegion.split(imgAnimHero,
                imgAnimHero.getWidth() / 3,
                imgAnimHero.getHeight() / 4);

        animationMarcheH = new Animation<TextureRegion>(0.15f, texturesHero[0]);
        animationMarcheD = new Animation<TextureRegion>(0.15f, texturesHero[1]);
        animationMarcheB = new Animation<TextureRegion>(0.15f, texturesHero[2]);
        animationMarcheG = new Animation<TextureRegion>(0.15f, texturesHero[3]);

        this.hero = new Sprite(animationMarcheD.getKeyFrame(0, true));
    }

    /**
     * Met à jour les données "pyhsiqueé du héros
     * @param deltaTime
     */
    @Override
    public void update(float deltaTime) {
        updateMotion(deltaTime);
    }

    /**
     * Permet de dessiner le héros dans la fenêtre
     * @param batch
     * @param deltaTime
     */
    @Override
    public void draw(SpriteBatch batch, float deltaTime) {
        // Update du sprite à afficher
        if (velocite.x == 0f && velocite.y == 0f) {
            hero.setRegion(texturesHero[2][1]);
        } else if (velocite.x > 0) {
            hero.setRegion(animationMarcheD.getKeyFrame(deltaTime, true));
        } else if (velocite.x < 0) {
            hero.setRegion(animationMarcheG.getKeyFrame(deltaTime, true));
        } else if (velocite.y > 0) {
            hero.setRegion(animationMarcheH.getKeyFrame(deltaTime, true));
        } else if (velocite.y < 0) {
            hero.setRegion(animationMarcheB.getKeyFrame(deltaTime, true));
        }

        hero.setSize(width, height);
        hero.setPosition(position.x, position.y);
        hero.draw(batch);
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
        if (leftMove) {
            velocite.set(-0.09f, velocite.y);
        }

        if (rightMove) {
            velocite.set(0.09f, velocite.y);
        }

        if (upMove) {
            velocite.set(velocite.x, 0.09f);
        }

        if (downMove) {
            velocite.set(velocite.x, -0.09f);
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


        velocite.nor();
        velocite.scl(vitesse);
        position.add(velocite);
    }
}
