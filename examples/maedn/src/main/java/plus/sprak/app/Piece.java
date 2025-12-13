package plus.sprak.app;

import plus.jboard.core.GameObject;
import plus.jboard.math.Vector2D;
import plus.jboard.render.Sprite;

import java.io.IOException;
import java.nio.file.Path;

public class Piece extends GameObject {

    private final int id;

    public Piece(int id) throws MaednException {
        this.id = id;
        try {
            this.sprite = new Sprite(Path.of("src", "main", "resources", "piece_green.png").toUri());
        } catch (IOException ignored) {
            throw new MaednException("Could not load piece.png");
        }
    }

    @Override
    public void onMouseClick(Vector2D position) {
        System.out.println("Piece #" + id + " clicked!");
    }
}
