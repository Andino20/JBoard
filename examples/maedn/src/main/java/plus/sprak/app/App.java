package plus.sprak.app;

import plus.jboard.core.GameApplication;
import plus.jboard.core.Piece;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class App {
    public static void main(String[] args) throws IOException {
        BufferedImage knight = ImageIO.read(new File(Path.of("src", "main", "resources", "knight.png").toUri()));
        GameApplication app = new GameApplication("Mensch-ärgere-dich-nicht!");

        for(int i = 0; i < 10; i++) {
            Piece p = new Piece(knight);
            p.getTransform().translate(i * 20, i * 20);
            app.addPiece(p);
        }

        app.run();
    }
}