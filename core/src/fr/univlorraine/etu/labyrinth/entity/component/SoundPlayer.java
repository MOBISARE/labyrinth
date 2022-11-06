package fr.univlorraine.etu.labyrinth.entity.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public final class SoundPlayer implements Component {

    private final Sound value;

    public SoundPlayer(String soundPath) {
        this.value = Gdx.audio.newSound(Gdx.files.internal(soundPath));
    }
}
