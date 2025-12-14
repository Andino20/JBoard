package plus.jboard.core;

import plus.jboard.core.facade.MouseClickListener;
import plus.jboard.math.Vector2D;
import plus.jboard.render.RenderContext;
import plus.jboard.render.RenderObject;

import javax.swing.*;
import java.util.Iterator;
import java.util.List;

public class GameApplication {

    private static final double FPS = 60.0;
    private static final double FRAME_TIME = 1_000_000_000 / FPS;

    private RenderContext renderer;
    private final Scene currentScene;

    public GameApplication(String title, int width, int height, Scene startingScene) {
        this.currentScene = startingScene;
        SwingUtilities.invokeLater(() -> {
            JFrame window = initFrame(title, width, height);
            renderer = new RenderContext();
            renderer.addMouseListener((MouseClickListener) e -> notifyMouseClick(Vector2D.of(e.getX(), e.getY())));
            window.add(renderer);
        });
    }

    private void notifyMouseClick(Vector2D position) {
        for (Iterator<GameObject> it = currentScene.getGameObjectsReversed(); it.hasNext(); ) {
            GameObject g = it.next();
            if (g.getBoundingBox().isInside(position)) {
                g.onMouseClick(position);
                break;
            }
        }
    }

    public void run() {
        long lastTime = System.nanoTime();
        double delta = 0;

        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / FRAME_TIME;
            lastTime = now;

            while (delta >= 1) {
                List<RenderObject> renderObjects = currentScene.getGameObjects().stream()
                        .map(GameObject::toRenderObject)
                        .toList();
                SwingUtilities.invokeLater(() -> renderer.render(renderObjects));
                delta--;
            }
        }
    }

    private static JFrame initFrame(String title, int width, int height) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return frame;
    }

}
