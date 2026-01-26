package plus.jboard.render;

import lombok.Getter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;

@Getter
public class Sprite {

    private final Image image;

    public Sprite(URI uri) throws IOException {
        image = ImageIO.read(new File(uri));
    }

    public Sprite(Image image) {
        this.image = image;
    }

    public int getWidth() {
        return image.getWidth(null);
    }

    public int getHeight() {
        return image.getHeight(null);
    }
}
