package fr.univlorraine.etu.labyrinth.entity;

import fr.univlorraine.etu.labyrinth.entity.component.Component;

import java.util.HashMap;
import java.util.Map;

public final class Entity {

    private final String name;

    private final String groupName;

    private final Map<Class<?>, Component> components;

    public Entity(String name, String groupName) {
        this.name = name;
        this.groupName = groupName;
        this.components = new HashMap<>();
    }

    public Entity(String name) {
        this(name, "<unknown>");
    }

    public <C extends Component> void addComponent(C component) {
        this.components.put(component.getClass(), component);
    }

    public <C extends Component> boolean hasComponent(Class<C> componentType) {
        return this.components.containsKey(componentType);
    }

    public <C extends Component> C removeComponent(Class<C> componentType) {
        return (C) this.components.remove(componentType);
    }

    public <C extends Component> C getComponent(Class<C> componentType) {
        return (C) this.components.get(componentType);
    }

    public String getName() {
        return name;
    }

    public String getGroupName() {
        return groupName;
    }
}
