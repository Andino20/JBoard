package plus.jboard.core;

import plus.jboard.render.Drawable;
import plus.jboard.render.RenderContext;

import javax.swing.*;
import java.util.List;
import java.util.TreeMap;

public class GameApplication {
    private final RenderContext renderer;
    private final Scene currentScene;
    private static final double FPS = 60.0;
    private static final double FRAME_TIME = 1_000_000_000 / FPS;

    public GameApplication(String title, int width, int height, Scene startingScene) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        renderer = new RenderContext();
        frame.add(renderer);
        this.currentScene = startingScene;
    }

    public void run() {
        long lastTime = System.nanoTime();
        double delta = 0;

        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / FRAME_TIME;
            lastTime = now;

            while (delta >= 1) {
                renderer.render(currentScene.getRenderObjects());
                delta--;
            }
        }

    }

}
