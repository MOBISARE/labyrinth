package com.mygdx.labyrinth.entity.component;

import com.badlogic.gdx.audio.Sound;

public final class SoundPlayer implements Component {

    private final Sound sound;
    private final int cooldown;
    private int deltaSound;

    public SoundPlayer(Sound sound, int deltaSound) {
        this.sound = sound;
        this.deltaSound = deltaSound;
        this.cooldown = deltaSound;
    }

    public SoundPlayer(Sound sound) {
        this.sound = sound;
        this.deltaSound = 0;
        this.cooldown = 0;
    }

    public Sound getSound() { return sound; }

    public void cooldown() { if(deltaSound != 0) deltaSound--; }
    public void restartCooldown() { deltaSound = cooldown; }
    public boolean operational() { return deltaSound == 0;}
}
