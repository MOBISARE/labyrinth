package com.mygdx.labyrinth.model.event;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.function.Consumer;

public class TimeEvent implements Event{

    private static final long UPDATE_INTERVAL_MS = 1000;

    private final Consumer<Float> action;
    private final long initialDelay;

    private long timer;

    private long delay;

    private boolean finish;

    public TimeEvent(Consumer<Float> action, long delay) {
        this.action = action;
        this.initialDelay = delay;
        this.timer = TimeUtils.millis();
        this.delay = 0;
        this.finish = false;
    }

    @Override
    public void update() {
        if (!this.finish) {
            if (TimeUtils.millis() - this.timer >= UPDATE_INTERVAL_MS) {
                this.timer += UPDATE_INTERVAL_MS;
                this.delay += UPDATE_INTERVAL_MS;
            }
           if (this.delay >= this.initialDelay) {
                this.action.accept(Gdx.graphics.getDeltaTime());
                this.finish = true;
            }
        }
    }

    @Override
    public boolean isFinished() {
        return finish;
    }

    @Override
    public void reset() {
        this.finish = false;
        this.timer = System.currentTimeMillis();
        this.delay = 0;
    }
}
