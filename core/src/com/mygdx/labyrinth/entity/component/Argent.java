package com.mygdx.labyrinth.entity.component;

public class Argent implements Component{

    private int argent;

    public Argent(int a) {
        this.argent = a;
    }

    public int getArgent() {
        return argent;
    }

    public void setArgent(int argent) {
        if (argent < 0) {
            this.argent = 0;
        } else {
            this.argent = argent;
        }
    }
}
