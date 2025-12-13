package plus.jboard.core;

import plus.jboard.math.Vector2D;
import plus.jboard.render.RenderContext;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;

public class GameApplication {

    private static final double FPS = 60.0;
    private static final double FRAME_TIME = 1_000_000_000 / FPS;

    private final RenderContext renderer;
    private final Scene currentScene;

    public GameApplication(String title, int width, int height, Scene startingScene) {
        this.currentScene = startingScene;
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        renderer = new RenderContext();
        renderer.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                for (Iterator<GameObject> it = currentScene.getGameObjectsReversed(); it.hasNext(); ) {
                    GameObject g = it.next();
                    if (g.getBoundingBox().isInside(Vector2D.of(e.getX(), e.getY()))) {
                        g.onMouseClick(Vector2D.of(e.getX(), e.getY()));
                        break;
                    }
                }
                System.out.println(Vector2D.of(e.getX(), e.getY()));
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        frame.add(renderer);
    }

    public void run() {
        long lastTime = System.nanoTime();
        double delta = 0;

        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / FRAME_TIME;
            lastTime = now;

            while (delta >= 1) {
                renderer.render(currentScene.getGameObjects());
                delta--;
            }
        }

    }

}
