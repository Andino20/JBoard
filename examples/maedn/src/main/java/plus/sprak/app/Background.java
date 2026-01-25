package plus.sprak.app;

import plus.jboard.core.GameObject;
import plus.jboard.render.Sprite;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Background extends GameObject {

    public Background() throws IOException {
        Image img = ImageIO.read(new File(Path.of("src", "main", "resources", "board.jpg").toUri()));
        this.sprite = new Sprite(img.getScaledInstance(720, 480, Image.SCALE_SMOOTH));
    }

}
