package com.mygdx.labyrinth.model;

import com.mygdx.labyrinth.controller.Observer;
import com.mygdx.labyrinth.exception.LabyrinthException;

import java.util.*;

public abstract class Observable {

    private static Map<String, Set<Observer>> observers;

    protected Observable() {
        observers = new HashMap<>();
    }

    public void addObserver(String eventType, Observer observer) {
        if (observers.containsKey(eventType)) {
            observers.get(eventType).add(observer);
        } else {
            HashSet<Observer> liste = new HashSet<>();
            liste.add(observer);
            observers.put(eventType, liste);
        }
    }

    public void removeObserver(String eventType, Observer observer) throws LabyrinthException {
        if (observers.containsKey(eventType)) {
            observers.get(eventType).remove(observer);
            if (observers.get(eventType).size() == 0) {
                observers.remove(eventType);
            }
        } else {
            throw new LabyrinthException("La clé n'existe pas .");
        }
    }

    public void notifierObservers(String eventType, Object obj) throws LabyrinthException {
        if (observers.containsKey(eventType)) {
            for (Observer o: observers.get(eventType)) {
                o.update(obj);
            }
        } else {
            throw new LabyrinthException("La clé n'existe pas");
        }
    }
}
