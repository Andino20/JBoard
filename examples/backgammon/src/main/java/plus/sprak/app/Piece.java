package plus.sprak.app;

import plus.jboard.core.GameObject;
import plus.jboard.math.Vector2D;
import plus.jboard.render.Sprite;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

public class Piece extends GameObject {

    private final PieceColor color;
    private int fieldPosition;
    private Consumer<Piece> moveListener;

    public Piece(PieceColor color) throws IOException {
        this.color = color;
        this.fieldPosition = switch (color) {
            case WHITE -> -1;
            case BLACK -> 24;
        };

        String filename = switch (color) {
            case WHITE -> "piece_white.png";
            case BLACK -> "piece_black.png";
        };
        this.sprite = new Sprite(Path.of("src", "main", "resources", filename).toUri());
    }

    public PieceColor getColor() {
        return color;
    }

    public int getFieldPosition() {
        return fieldPosition;
    }

    public void setFieldPosition(int fieldPosition) {
        this.fieldPosition = fieldPosition;
    }

    public void setMoveListener(Consumer<Piece> listener) {
        this.moveListener = listener;
    }

    @Override
    public void onMouseClick(Vector2D position) {
        if (moveListener != null) {
            moveListener.accept(this);
        }
    }

}
