package plus.sprak.app;

import plus.jboard.core.GameObject;
import plus.jboard.core.Scene;
import plus.jboard.render.Sprite;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class BackgammonScene extends Scene {

    public static class Background extends GameObject {
        public Background() throws IOException {
            Image img = ImageIO.read(new File(Path.of("src", "main", "resources", "board.jpg").toUri()));
            this.sprite = new Sprite(img.getScaledInstance(720, 480, Image.SCALE_SMOOTH));
        }
    }
    public BackgammonScene() throws IOException {
        Background bg = new Background();
        Board board = new Board(new Die());
        board.getAllPieces().forEach(p -> this.addGameObject(p, 1));
        this.addGameObject(bg, 0);
    }

}
