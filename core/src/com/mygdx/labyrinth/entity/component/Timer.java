package com.mygdx.labyrinth.entity.component;

public class Timer {

    private long lastTime;

    private boolean isActif;

    public Timer() {
        this.lastTime = System.currentTimeMillis();
        this.isActif= false;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public boolean isActif() {
        return isActif;
    }

    public void setActif(boolean actif) {
        isActif = actif;
    }

    public void reset() {
        lastTime = System.currentTimeMillis();
    }
}
