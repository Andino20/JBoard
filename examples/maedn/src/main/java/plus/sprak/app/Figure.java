package plus.sprak.app;

import plus.jboard.core.GameObject;
import plus.jboard.math.Vector2D;
import plus.jboard.render.Sprite;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

public class Figure extends GameObject {

    private final PieceColor color;
    private int fieldPosition;
    private Consumer<Figure> moveListener;

    public Figure(PieceColor color) throws IOException {
        this.color = color;
        this.fieldPosition = 0;

        String filename = "";
        switch (color) {
            case RED -> filename = "piece_red.png";
            case GREEN -> filename = "piece_green.png";
            case YELLOW -> filename = "piece_yellow.png";
            case BLUE -> filename = "piece_blue.png";
        }
        this.sprite = new Sprite(Path.of("src", "main", "resources", filename).toUri());
    }

    @Override
    public void onMouseClick(Vector2D position) {
        if (moveListener != null) {
            moveListener.accept(this);
        }
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

    public void setMoveListener(Consumer<Figure> listener) {
        this.moveListener = listener;
    }

}
