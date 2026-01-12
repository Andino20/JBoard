package plus.jboard.core;

import javax.swing.*;
import java.util.*;
import java.util.List;

public class Scene {

    private final TreeMap<Integer, List<GameObject>> gameObjects = new TreeMap<>();
    private JPanel uiPanel;

    protected List<GameObject> getGameObjects() {
        return gameObjects.values()
                .stream()
                .flatMap(List::stream)
                .toList();
    }

    protected Iterator<GameObject> getGameObjectsReversed() {
        return gameObjects.reversed().values()
                .stream()
                .flatMap(List::stream)
                .iterator();
    }

    protected Optional<JPanel> getUI() {
        return Optional.ofNullable(uiPanel);
    }

    public void setUI(JPanel ui) {
        this.uiPanel = ui;
    }

    public void addGameObject(GameObject d, int zIndex) {
        gameObjects.computeIfAbsent(zIndex, z -> new ArrayList<>()).add(d);
    }
}
