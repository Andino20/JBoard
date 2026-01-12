package plus.jboard.core;

import lombok.Getter;
import plus.jboard.core.facade.MouseClickListener;
import plus.jboard.math.Vector2D;
import plus.jboard.net.NetworkMessage;
import plus.jboard.net.handler.MessageCollector;
import plus.jboard.net.handler.MessageDispatcher;
import plus.jboard.render.RenderContext;
import plus.jboard.render.RenderObject;

import javax.swing.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class GameApplication {

    private static final double FPS = 60.0;
    private static final double FRAME_TIME = 1_000_000_000 / FPS;

    private JFrame window;

    private RenderContext renderer;
    private Scene currentScene;

    private final List<Updatable> updatables = new LinkedList<>();

    @Getter
    private final MessageCollector messageCollector = new MessageCollector();

    @Getter
    private final MessageDispatcher messageDispatcher = new MessageDispatcher();

    @Getter
    private static GameApplication instance;

    public GameApplication(String title, int width, int height, Scene startingScene) {
        this.currentScene = startingScene;
        SwingUtilities.invokeLater(() -> {
            window = initFrame(title, width, height);
            renderer = new RenderContext();
            renderer.addMouseListener((MouseClickListener) e -> notifyMouseClick(Vector2D.of(e.getX(), e.getY())));
            window.add(renderer);
            currentScene.getUI().ifPresent(window::add);
        });
        instance = this;
        updatables.add(messageDispatcher);
    }

    public void switchScenes(Scene newScene) {
        Scene oldScene = currentScene;
        this.currentScene = newScene;
        SwingUtilities.invokeLater(() -> {
            oldScene.getUI().ifPresent(window::remove);
            currentScene.getUI().ifPresent(window::add);
            window.revalidate();
            window.repaint();
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
                update();
                processNetworkMessages();
                render();
                delta--;
            }
        }
    }

    private void update() {
        updatables.forEach(Updatable::update);
    }

    public void addUpdatable(Updatable u) {
        this.updatables.add(u);
    }

    private void render() {
        List<RenderObject> renderObjects = currentScene.getGameObjects().stream()
                .map(GameObject::toRenderObject)
                .toList();
        SwingUtilities.invokeLater(() -> renderer.render(renderObjects));
    }

    /**
     * Processes up to 10 buffered network messages by passing them on to the message handlers,
     * if an appropriate one for that type of message is registered with the dispatcher.
     */
    private void processNetworkMessages() {
        NetworkMessage msg;
        int counter = 0;
        while ((msg = messageCollector.poll()) != null && counter < 10) {
            messageDispatcher.dispatch(msg);
            counter++;
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
