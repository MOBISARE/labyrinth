package com.mygdx.labyrinth.model.collision;

import java.util.*;

/**
 * Classe qui permet de détécter les collision
 */
public class CollisionDetector {

    //region Attributs

    /**
     * Liste des body qui ne bougent pas (pas besoin de checker si il entrent en collision avec quelque chose)
     */
    private HashSet<Body> staticBodies;

    /**
     * Liste des body qui se déplacent
     */
    private HashSet<Body> dynamicBodies;
    /**
     * Liste qui contient l'ensemble des événements de collision pour les transmettre au cCollisionManager
     */
    private static LinkedList<CollisionEvent> collisionEvents;

    //endregion

    /**
     * Constructeur par défaut
     */
    public CollisionDetector() {
        this.staticBodies = new HashSet<>();
        this.dynamicBodies = new HashSet<>();
        collisionEvents = new LinkedList<>();
    }

    /**
     * Ajout un body soit dans la liste des body static soit celle dynamique
     * @param b Body
     */
    public void addBody(Body b) {
        if (b.isDynamic()) {
            dynamicBodies.add(b);
        } else {
            staticBodies.add(b);
        }
    }

    /**
     * Permet de retirer un body d'une des listes
     * @param b Body
     */
    public void removeBody(Body b) {
        staticBodies.remove(b);
        dynamicBodies.remove(b);
    }

    /**
     * Algorithme principale de détection des collisions
     */
    public void detecteCollision() {
        dynamicBodies.removeIf(Body::isDestroyed);
        staticBodies.removeIf(Body::isDestroyed);

        Body[] tab = dynamicBodies.toArray(Body[]::new);

        // On regarde d'abord si les objets qui bougent entrent en collision
        for (int i = 0; i < dynamicBodies.size(); i++) {
            for (int j = i + 1; j < dynamicBodies.size(); j++) {
                if (tab[i].getBounds().overlaps(tab[j].getBounds())) {
                    collisionEvents.add(new CollisionEvent(tab[i], tab[j]));
                }
            }
        }
        // Ensuite on regarde si les body dynamique entrent en collision avec les éléments static
        for (Body b1 : dynamicBodies) {
            for (Body b2 : staticBodies) {
                if (b1.getBounds().overlaps(b2.getBounds())) {
                    collisionEvents.add(new CollisionEvent(b1, b2));
                }
            }
        }
    }

    /**
     * Retourne l'ensemble des body de chaque liste dans un seul set
     * @return HashSet<Body>
     */
    public HashSet<Body> getBodies() {
        HashSet<Body> res =  new HashSet<>();
        res.addAll(staticBodies);
        res.addAll(dynamicBodies);
        return res;
    }

    /**
     * Retourne le nombre d'événements de collision
     * @return int
     */
    public int getEventsSize() {
        return collisionEvents.size();
    }

    /**
     * Supprime et retourne l'élément en tête de la liste des événements
     * @return CollisionEvent
     */
    public CollisionEvent removeFirstEvent() {
        return collisionEvents.removeFirst();
    }

    /**
     * Vide et supprime toutes les listes
     */
    public void dispose() {
        dynamicBodies.clear();
        dynamicBodies = null;
        staticBodies.clear();
        staticBodies = null;
        collisionEvents.clear();
        collisionEvents = null;
    }
}
