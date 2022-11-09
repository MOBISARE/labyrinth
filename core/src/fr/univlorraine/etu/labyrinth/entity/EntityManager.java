package fr.univlorraine.etu.labyrinth.entity;

import fr.univlorraine.etu.labyrinth.entity.component.Component;
import fr.univlorraine.etu.labyrinth.entity.component.DynamicBody;

import java.util.*;
import java.util.stream.Collectors;

public final class EntityManager {
    private final List<Entity> entities;

    private final Set<Entity> dynamicBodies;

    private final Set<Entity> staticBodies;

    public EntityManager() {
        this.entities = new ArrayList<>();
        this.dynamicBodies = new HashSet<>();
        this.staticBodies = new HashSet<>();
    }

    public void add(Entity entity) {
        this.entities.add(entity);
    }

    public void sortBodies() {
        for (Entity e : this.entities) {
            if (e.hasComponent(DynamicBody.class)) {
                this.dynamicBodies.add(e);
            } else {
                this.staticBodies.add(e);
            }
        }
    }

    public List<Entity> findByGroupName(String groupName) {
        return this.entities
                .stream()
                .filter(e -> Objects.equals(groupName, e.getGroupName()))
                .collect(Collectors.toList());
    }

    public Entity findByName(String name) {
        return this.entities
                .stream()
                .filter(e -> Objects.equals(name, e.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Entité non trouvée pour le nom : " + name));
    }

    public <C extends Component> C findByNameAndComponent(String name, Class<C> componentType) {
        return this.findByName(name).getComponent(componentType);
    }

    public <C extends Component> List<C> findByComponent(Class<C> componentType) {

//        List<C> components = new ArrayList<>();
//        for(Entity e: this.entities){
//            if(e.hasComponent(componentType)){
//                C component = e.getComponent(componentType);
//                components.add(component);
//            }
//        }
//        return components;

        return this.entities
                .stream()
                .filter(e -> e.hasComponent(componentType))
                .map(e -> e.getComponent(componentType))
                .collect(Collectors.toList());
    }

    public <C extends Component> Entity findByComponent(C component) {
        Class<C> componentType = (Class<C>) component.getClass();
        return this.entities
                .stream()
                .filter(e -> e.hasComponent(componentType))
                .filter(e -> Objects.equals(e.getComponent(componentType), component))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Entité non trouvée pour le composant : " + componentType + " " + component));
    }

    public void removeByName(String name) {
        Entity entity = this.entities
                .stream()
                .filter(e -> Objects.equals(e.getName(), name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Entité non trouvée pour le nom : " + name));
        this.entities.remove(entity);
        this.staticBodies.remove(entity);
        this.dynamicBodies.remove(entity);
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Set<Entity> getDynamicBodies() {
        return dynamicBodies;
    }

    public Set<Entity> getStaticBodies() {
        return staticBodies;
    }
}
