package com.mygdx.labyrinth.model.collision;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.labyrinth.exception.LabyrinthException;
import com.mygdx.labyrinth.model.level.Level0;
import com.mygdx.labyrinth.model.Entity;


/**
 * Classe qui gére l'ensemble des collisions du jeu
 */
public class CollisionManager {

    //region Attributs

    /**
     * Utilise un collisionDetector
     */
    private final CollisionDetector collisionDetector;
    /**
     * Niveau associé
     */
    private final Level0 level;

    /**
     * Moteur de rendu du debeggeur
     */
    private final ShapeRenderer shapeRenderer;

    //endregion


    /**
     * Constructeur par défaut
     * @param l Level0
     */
    public CollisionManager(Level0 l) {
        this.level = l;
        this.collisionDetector = new CollisionDetector();
        this.shapeRenderer = new ShapeRenderer();
    }

    /**
     * Initialise les données pour la gestion de collision
     * @throws LabyrinthException indique si une entité ne possède pas de body
     */
    public void init() throws LabyrinthException {

        // Ici on récupère l'ensemble des entité du niveau
        for (Entity e: level) {
            if (e.getBody() != null) {
                collisionDetector.addBody(e.getBody());
            } else {
                throw new LabyrinthException("Un Body n'est pas défini");
            }
        }

        // ici on récupère l'ensemble des objets de la map
        for (MapObject m: level.getCollisionObjectOnMap()) {
            if (m.getName().equals("wall")) {
                float x = m.getProperties().get("x", float.class) / 16f;
                float y = m.getProperties().get("y", float.class) / 16f;
                float width = m.getProperties().get("width", float.class) / 16f;
                float height = m.getProperties().get("height", float.class) / 16f;


                Rectangle r = new Rectangle( x, y , width , height );
                collisionDetector.addBody(new Body(r, BodyType.WALL, null, false));
            }
        }
    }

    /**
     * Avance le "monde" d'une étape
     */
    public void step() {
        collisionDetector.detecteCollision();
        while (collisionDetector.getEventsSize() > 0) {
            CollisionEvent ce = collisionDetector.removeFirstEvent();
            /* Cette partie est importante et défini le sens de la collision
             * il faudra traiter le cas lorsque b1 entre en collision avec b2 et inversement
             */
            ce.getBody1().getEntityParent().handleCollision(ce.getBody2());
        }
    }

    /**
     * Permet d'affiché la hitbox des éléments qui sont pris en compte lors de la détéction de collisions
     * @param camera OrthographicCamerz
     */
    public void debugRenderer(OrthographicCamera camera) {
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        for (Body b : collisionDetector.getBodies()) {
            shapeRenderer.rect(b.getBounds().x, b.getBounds().y, b.getBounds().width, b.getBounds().height);
        }
        shapeRenderer.end();
    }

    /**
     * Libère les ressources
     */
    public void dispose() {
        shapeRenderer.dispose();
        collisionDetector.dispose();
    }
}
