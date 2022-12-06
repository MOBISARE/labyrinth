package com.mygdx.labyrinth.entity.component.features;

import com.mygdx.labyrinth.entity.component.Component;

public class Vie implements Component {

    private int vie;

    public Vie(int vie) {
        this.vie = vie;
    }

    public int getVie() {
        return vie;
    }

    public void setVie(int vie) {
        if (vie < 0) {
            this.vie = 0;
        } else {
            this.vie = vie;
        }
    }
}
