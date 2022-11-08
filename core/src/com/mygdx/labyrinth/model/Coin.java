package com.mygdx.labyrinth.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.labyrinth.model.collision.Body;
import com.mygdx.labyrinth.model.collision.BodyType;

/**
 * Classe qui représente les pièces de monnaies
 */
public class Coin implements Entity {

    //region Attributs

    /**
     * Textures d'une pièce
     */
    private final Texture imgCoin;

    /**
     * Animation d'une pièce
     */
    private final Animation<TextureRegion> animationCoin;

    /**
     * Body d'une pièce
     */
    private final Body body;

    /**
     * Necessaire pour récupérer la bonne image dans l'annimation
     */
    private float stateTime = 0f;

    /**
     * Etat de l'entité
     */
    private boolean isDestroyed;

    /**
     * Son de récupération d'une pièce
     */
    private Sound sound;

    //endregion

    /**
     * Constructeur
     * @param x float pos x
     * @param y float pos y
     * @param width float largeur
     * @param height float hauteur
     */
    public Coin(float x, float y, float width, float height) {
        this.isDestroyed = false;
        this.body = new Body(new Rectangle(x, y, width, height), BodyType.COIN, this, false);
        this.imgCoin = new Texture(Gdx.files.internal("textures/animation_coin.png"));
        this.sound = Gdx.audio.newSound(Gdx.files.internal("sound/coin.wav"));
        TextureRegion[][] texturesCoin = TextureRegion.split(imgCoin, imgCoin.getWidth() / 4, imgCoin.getHeight());

        TextureRegion[] coin = {texturesCoin[0][0], texturesCoin[0][1], texturesCoin[0][2], texturesCoin[0][3]};
        animationCoin = new Animation<>(0.12f, coin );
    }

    /**
     * {@inheritDoc}
     * @param batch SpriteBatch, batch utilisé pour le rendu
     * @param deltaTime float, temps écoulé depuis la dernière image affichée
     */
    @Override
    public void render(SpriteBatch batch, float deltaTime) {
        stateTime += deltaTime;

        batch.draw(animationCoin.getKeyFrame(stateTime, true),
                body.getBounds().x,
                body.getBounds().y,
                body.getBounds().width,
                body.getBounds().height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        this.imgCoin.dispose();
        this.isDestroyed = true;
        sound.play(0.2f);
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public Body getBody() {
        return body;
    }

    /**
     * {@inheritDoc}
     * @param b Body
     */
    @Override
    public void handleCollision(Body b) {

    }

    @Override
    public boolean isDestroyed() {
        return isDestroyed;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Coin{");
        sb.append("body=").append(body);
        sb.append('}');
        return sb.toString();
    }
}
