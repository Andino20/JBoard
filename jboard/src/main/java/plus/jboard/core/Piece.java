package plus.jboard.core;

import plus.jboard.plus.jboard.components.Transform;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Piece {
    private Transform transform;
    private BufferedImage image;

    public Piece(BufferedImage image) {
        this.image = image;
        this.transform = new Transform(0, 0);
    }

    public Transform getTransform() {
        return transform;
    }

    public Image getImage() {
        return image;
    }
}
