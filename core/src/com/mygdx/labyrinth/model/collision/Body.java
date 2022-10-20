package com.mygdx.labyrinth.model.collision;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.labyrinth.model.Entity;

/**
 * Classe qui définit un body et est utilisé pour la gestion des collision
 */
public class Body {

    //region Attributs

    /**
     * Définit la hitbox d'un objet
     */
    private final Rectangle bounds;
    /**
     * C'est le type de body
     */
    private final BodyType bodyType;
    /**
     * Entité a laquelle est reliée le body
     * c'est null pour les blocs de la map
     */
    private final Entity entityParent;
    /**
     * Indique si l'entité bouge ou pas
     */
    private final boolean isDynamic;

    /**
     * Indique si l'entité est détruite
     */
    private boolean isDestroyed;

    //endregion

    /**
     * Construit un body
     * @param r Rectangle
     * @param t BodyType
     * @param e Entity
     * @param isDynamic boolean
     */
    public Body(Rectangle r, BodyType t, Entity e, boolean isDynamic) {
        this.bounds = r;
        this.bodyType = t;
        entityParent = e;
        this.isDynamic = isDynamic;
        this.isDestroyed = false;
    }

    /**
     * Set x
     * @param x float
     */
    public void setPosX(float x) {
        bounds.setX(x);
    }

    /**
     * Set y
     * @param y float
     */
    public void setPosY(float y) {
        bounds.setY(y);
    }

    /**
     * Retourne le type du body
     * @return BodyType
     */
    public BodyType getBodyType() {
        return bodyType;
    }

    /**
     * Retourne le rectangle qui compose le body
     * @return Rectangle
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Retourne l'entité associé au body
     * @return Entity
     */
    public Entity getEntityParent() {
        return entityParent;
    }

    /**
     * Retourne vrai si le body se déplace sur la map
     * @return boolean
     */
    public boolean isDynamic() {
        return isDynamic;
    }

    /**
     * Permet de détruire un body, celui-ci sera retiré du collision detector si c'est le cas
     */
    public void destroyed() {
        this.isDestroyed = true;
    }

    /**
     * Retourne vrai si le corps est détruit
     * @return boolean
     */
    public boolean isDestroyed() {
        return isDestroyed;
    }
}
