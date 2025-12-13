package plus.jboard.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

public class Scene {

    private final TreeMap<Integer, List<GameObject>> gameObjects = new TreeMap<>();

    protected Iterator<GameObject> getGameObjects() {
        return gameObjects.values()
                .stream()
                .flatMap(List::stream)
                .iterator();
    }

    protected Iterator<GameObject> getGameObjectsReversed() {
        return gameObjects.reversed().values()
                .stream()
                .flatMap(List::stream)
                .iterator();
    }

    public void addGameObject(GameObject d, int zIndex) {
        gameObjects.computeIfAbsent(zIndex, z -> new ArrayList<>()).add(d);
    }
}
