package com.mygdx.labyrinth.entity.component.sound;

import com.badlogic.gdx.audio.Music;
import com.mygdx.labyrinth.entity.component.Component;

public final class MusicLevel implements Component {

    private final Music music;

    public MusicLevel(Music music) {
        this.music = music;
    }

    public void init() {
        music.setVolume(0.02f);
        music.setLooping(true);
        music.play();
    }

    public Music getSound() { return music; }
}
