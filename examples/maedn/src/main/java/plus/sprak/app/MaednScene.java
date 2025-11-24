package plus.sprak.app;

import plus.jboard.core.Scene;
import plus.jboard.render.Drawable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MaednScene extends Scene {

    public static class GameObject implements Drawable {
        private Image image;
        private int x;
        private int y;

        public GameObject(Image image, int x, int y) {
            this.image = image;
            this.x = x;
            this.y = y;
        }

        @Override
        public void draw(Graphics2D g2d) {
            g2d.drawImage(image, x, y, null);
        }
    }

    public MaednScene() throws IOException {
        BufferedImage piece = ImageIO.read(new File(Path.of("src", "main", "resources", "piece.png").toUri()));
        Image background = ImageIO.read(new File(Path.of("src", "main", "resources", "board.jpg").toUri()));

        background = background.getScaledInstance(720, 480, Image.SCALE_SMOOTH);

        ArrayList<GameObject> pieces = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            pieces.add(new GameObject(piece, i * 64, i * 64));
        }

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                pieces.get(1).x += 64;
                pieces.get(1).x %= 64 * 10;
            }
        }, 0, 2000);

        this.addRenderObject(new GameObject(background, 0, 0), 0);
        for (GameObject g : pieces) {
            this.addRenderObject(g, 1);
        }
    }

}
