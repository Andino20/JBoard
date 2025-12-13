package plus.sprak.app;

import plus.jboard.core.GameObject;
import plus.jboard.core.Scene;
import plus.jboard.math.Vector2D;
import plus.jboard.render.Sprite;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

public class MaednScene extends Scene {

    public static class Background extends GameObject {
        public Background() throws IOException {
            this.sprite = new Sprite(Path.of("src", "main", "resources", "board.jpg").toUri());
        }
    }

    public MaednScene() throws IOException {
        ArrayList<Piece> pieces = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Piece p = new Piece(i);
            p.setPosition(new Vector2D(i * 64, i * 64));
            pieces.add(p);
        }
        BufferedImage piece = ImageIO.read(new File(Path.of("src", "main", "resources", "piece.png").toUri()));
        Image background = ImageIO.read(new File(Path.of("src", "main", "resources", "board.jpg").toUri()));
        Map<Integer, int[]> map = Map.ofEntries(
                Map.entry(-1, new int[]{15}),
                Map.entry(0, new int[]{0, 128}),
                Map.entry(1, new int[]{64, 128}),
                Map.entry(2, new int[]{128, 128}),
                Map.entry(3, new int[]{192, 128}),
                Map.entry(4, new int[]{256, 128}),
                Map.entry(5, new int[]{0, 192}),
                Map.entry(6, new int[]{64, 192}),
                Map.entry(7, new int[]{128, 192}),
                Map.entry(8, new int[]{192, 192}),
                Map.entry(9, new int[]{256, 192}),
                Map.entry(10, new int[]{0, 256}),
                Map.entry(11, new int[]{64, 256}),
                Map.entry(12, new int[]{128, 256}),
                Map.entry(13, new int[]{192, 256}),
                Map.entry(14, new int[]{256, 256}),
                Map.entry(-10, new int[]{512, 256}));
        Background bg = new Background();
        this.addGameObject(bg, 0);
    }

}
