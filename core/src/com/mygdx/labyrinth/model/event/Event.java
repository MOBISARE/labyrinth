package com.mygdx.labyrinth.model.event;

public interface Event {

    void update();

    boolean isFinished();

    void reset();
}
