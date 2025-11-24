package plus.jboard.core;

import plus.jboard.render.Drawable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

public class Scene {

    private final TreeMap<Integer, List<Drawable>> renderObjects = new TreeMap<>();

    protected Iterator<Drawable> getRenderObjects() {
        return renderObjects.values()
                .stream()
                .flatMap(List::stream)
                .iterator();
    }

    public void addRenderObject(Drawable d, int zIndex) {
        renderObjects.computeIfAbsent(zIndex, z -> new ArrayList<>()).add(d);
    }
}
