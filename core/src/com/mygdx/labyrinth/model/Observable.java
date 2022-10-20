package com.mygdx.labyrinth.model;

import com.mygdx.labyrinth.controller.Observer;
import com.mygdx.labyrinth.exception.LabyrinthException;

import java.util.*;

/**
 * Classe abstraite permettant de mettre en place le design pattern Observer
 */
public abstract class Observable {

    /**
     * Liste des observeurs en fonction d'un type d'évenement défini par la clé
     */
    private static Map<String, Set<Observer>> observers;

    /**
     * Constructeur
     */
    protected Observable() {
        observers = new HashMap<>();
    }

    /**
     * Ajoute un observateur
     * @param eventType type d'événement
     * @param observer l'observateur
     */
    public void addObserver(String eventType, Observer observer) {
        if (observers.containsKey(eventType)) {
            observers.get(eventType).add(observer);
        } else {
            HashSet<Observer> liste = new HashSet<>();
            liste.add(observer);
            observers.put(eventType, liste);
        }
    }

    /**
     * Retir un observateur
     * @param eventType type d'événement
     * @param observer l'observateur
     * @throws LabyrinthException ...
     */
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

    /**
     * Notifie tous les observateur en fonction du type d'événement et transmet des données
     * @param eventType type événement
     * @param obj données nécessaire pour la mise à jour
     * @throws LabyrinthException ...
     */
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
