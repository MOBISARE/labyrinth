package fr.univlorraine.etu.labyrinth.entity.component;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import fr.univlorraine.etu.labyrinth.engine.Engine;

public final class FollowingCamera implements Component {

    private final OrthographicCamera camera;

    private final Viewport viewport;

    public FollowingCamera() {
        this.camera = new OrthographicCamera();
        this.viewport = new ExtendViewport(Engine.WIDTH, Engine.HEIGHT, this.camera);
    }

    public void init() {
        this.camera.setToOrtho(false);
        this.viewport.apply();
        this.camera.position.set(
                this.camera.viewportWidth / 2,
                this.camera.viewportHeight / 2,
                0);
    }

    public void follow(float positionX, float positionY, int mapWidth, int mapHeight) {

        float vw = this.camera.viewportWidth / 2f;
        this.camera.position.x = MathUtils.clamp(positionX, vw, mapWidth - vw);

        float vh = this.camera.viewportHeight / 2f;
        this.camera.position.y = MathUtils.clamp(positionY, vh, mapHeight - vh);
        this.camera.update();
    }

    public void resize(int width, int height) {
        this.camera.viewportWidth = width;
        this.camera.viewportHeight = height;
    }


    public OrthographicCamera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }
}
