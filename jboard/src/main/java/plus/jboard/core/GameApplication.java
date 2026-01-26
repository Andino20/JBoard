package plus.jboard.core;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import plus.jboard.core.facade.MouseClickListener;
import plus.jboard.math.Vector2D;
import plus.jboard.net.NetworkEnvelope;
import plus.jboard.net.NetworkMessage;
import plus.jboard.net.handler.MessageCollector;
import plus.jboard.net.handler.MessageDispatcher;
import plus.jboard.render.RenderContext;
import plus.jboard.render.RenderObject;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public final class GameApplication implements Runnable {

    private static final double FPS = 30.0;
    private static final long FRAME_NANOS = (long) (1_000_000_000 / FPS);

    private JLayeredPane layeredPane;
    private RenderContext renderer;

    private volatile Scene currentScene = new Scene();

    private final List<Updatable> updatables = new CopyOnWriteArrayList<>();

    @Getter
    private static GameApplication instance;

    @Getter
    private final MessageCollector messageCollector = new MessageCollector();

    @Getter
    private final MessageDispatcher messageDispatcher = new MessageDispatcher();

    public GameApplication(String title, int width, int height, Scene startingScene) {
        if (instance != null) {
            throw new IllegalStateException("GameApplication already initialized");
        }
        instance = this;
        init(title, width, height, startingScene);
    }

    private void init(String title, int width, int height, Scene startingScene) {
        this.currentScene = startingScene;

        JFrame window = initFrame(title, width, height);
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);

        renderer = new RenderContext();
        renderer.setBounds(0, 0, width, height);

        layeredPane.add(renderer, Integer.valueOf(0));

        renderer.addMouseListener(
                (MouseClickListener) e ->
                        notifyMouseClick(Vector2D.of(e.getX(), e.getY()))
        );

        switchScenes(startingScene);

        window.setContentPane(layeredPane);
        window.setVisible(true);

        updatables.add(messageDispatcher);
    }

    private static JFrame initFrame(String title, int width, int height) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        return frame;
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();

        while (!Thread.currentThread().isInterrupted()) {
            long now = System.nanoTime();
            long elapsed = now - lastTime;

            if (elapsed >= FRAME_NANOS) {
                lastTime = now;
                update();
                processNetworkMessages();
                render();
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void switchScenes(Scene newScene) {
        Scene oldScene = this.currentScene;
        this.currentScene = newScene;

        SwingUtilities.invokeLater(() -> {
            log.info("Switching scenes from {} to {}",
                    oldScene.getClass().getSimpleName(),
                    newScene.getClass().getSimpleName());

            oldScene.getUI().ifPresent(layeredPane::remove);
            newScene.getUI().ifPresent(ui -> {
                ui.setBounds(renderer.getBounds());
                layeredPane.add(ui, Integer.valueOf(1));
            });

            layeredPane.revalidate();
            layeredPane.repaint();
        });
    }

    private void notifyMouseClick(Vector2D position) {
        for (var it = currentScene.getGameObjectsReversed(); it.hasNext(); ) {
            GameObject g = it.next();
            if (g.getBoundingBox().isInside(position)) {
                g.onMouseClick(position);
                break;
            }
        }
    }

    private void update() {
        updatables.forEach(Updatable::update);
    }

    public void addUpdatable(Updatable updatable) {
        updatables.add(updatable);
    }

    private void render() {
        List<RenderObject> renderObjects = currentScene.getGameObjects()
                .stream()
                .map(GameObject::toRenderObject)
                .toList();

        renderer.render(renderObjects);
        renderer.repaint();
    }

    private void processNetworkMessages() {
        NetworkEnvelope<NetworkMessage> envelope;
        int processed = 0;

        while (processed < 10 && (envelope = messageCollector.poll()) != null) {
            messageDispatcher.dispatch(envelope);
            processed++;
        }
    }
}
