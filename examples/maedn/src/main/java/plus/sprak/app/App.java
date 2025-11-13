package plus.sprak.app;

import plus.jboard.core.GameApplication;
import plus.jboard.render.Drawable;

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

    public record GameObject(BufferedImage image, int x, int y) implements Drawable {
        public GameObject(BufferedImage image, int x, int y) {
            this.image = image;
            this.x = x;
            this.y = y;
        }

        @Override
        public void draw(Graphics2D g2d) {
            g2d.drawImage(image, x, y, null);
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

        BufferedImage piece = ImageIO.read(new File(Path.of("src", "main", "resources", "piece.png").toUri()));
        List<GameObject> pieces = IntStream.range(0, 10)
                .mapToObj(p -> new GameObject(piece, p * 64, p * 64))
                .toList();
        GameApplication app = new GameApplication("Mensch-ärgere-dich-nicht!", 720, 480, pieces);
    }
}