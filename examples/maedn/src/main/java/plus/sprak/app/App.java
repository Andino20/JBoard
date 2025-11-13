package plus.sprak.app;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.TimerTask;
import java.util.stream.IntStream;


public class App {

    public record GameObject(BufferedImage image, int x, int y) {
        public GameObject(BufferedImage image, int x, int y) {
            this.image = image;
            this.x = x;
            this.y = y;
        }
    }

    public static class RenderContext extends JPanel {

        private List<GameObject> objects;

        public RenderContext() {
            this.setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            for (GameObject object : objects) {
                g2d.drawImage(object.image, object.x, object.y, null);
            }
        }

        public void setGameObjects(List<GameObject> objects) {
            this.objects = objects;
        }
    }

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Mensch-ärgere-dich-nicht!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(720, 480);
        frame.setLocationRelativeTo(null);

        RenderContext renderContext = new RenderContext();
        frame.add(renderContext);

        frame.setVisible(true);

        BufferedImage piece = ImageIO.read(new File(Path.of("src", "main", "resources", "piece.png").toUri()));
        List<GameObject> pieces1 = IntStream.range(0, 10)
                .mapToObj(p -> new GameObject(piece, p * 64, p * 64))
                .toList();
        List<GameObject> pieces2 = IntStream.range(0, 10)
                .mapToObj(p -> new GameObject(piece, p * 64, 10*64 - p * 64))
                .toList();
        renderContext.setGameObjects(pieces1);

        java.util.Timer t = new java.util.Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            List<GameObject> next = pieces2;

            @Override
            public void run() {
                renderContext.setGameObjects(next);
                next = (next == pieces1) ? pieces2 : pieces1;
                renderContext.repaint();
            }
        }, 0, 1500);

    }
}