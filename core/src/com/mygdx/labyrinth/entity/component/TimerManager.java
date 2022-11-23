package com.mygdx.labyrinth.entity.component;

import java.util.HashMap;
import java.util.Map;

public class TimerManager implements Component{

    private Map<String, Timer> timers;

    public TimerManager() {
        timers = new HashMap<>();
    }

    public void createTimer(String nom) {
        timers.put(nom, new Timer());
    }

    public long getLastTimeOf(String name) {
        return timers.get(name).getLastTime();
    }

    public boolean isActif(String name) {
        return timers.get(name).isActif();
    }

    public void setActif(String name, boolean activity) {
        timers.get(name).setActif(activity);
    }

    public void setLastTimeOf(String name, long time) {
        timers.get(name).setLastTime(time);
    }

    public void resetOf(String name) {
        timers.get(name).reset();
    }
}
