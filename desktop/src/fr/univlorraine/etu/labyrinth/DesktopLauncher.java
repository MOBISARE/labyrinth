package fr.univlorraine.etu.labyrinth;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import fr.univlorraine.etu.labyrinth.engine.Engine;

public final class DesktopLauncher {

    private DesktopLauncher() {
    }

    public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("Labyrinth");
        config.setWindowedMode(Engine.SCALE * Engine.WIDTH,Engine.SCALE * Engine.HEIGHT);
        config.setResizable(true);
        config.useVsync(true);
        new Lwjgl3Application(new Engine(), config);
    }

}
