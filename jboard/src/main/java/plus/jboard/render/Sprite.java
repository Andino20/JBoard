package plus.jboard.render;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class Sprite {

    private final Image image;

    public Sprite(URI uri) throws IOException {
        image = ImageIO.read(new File(uri));
    }

    public Sprite(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public int getWidth() {
        return image.getWidth(null);
    }

    public int getHeight() {
        return image.getHeight(null);
    }
}
