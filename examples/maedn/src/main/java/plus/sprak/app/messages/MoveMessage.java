package plus.sprak.app.messages;

import plus.sprak.app.PieceColor;

public class MoveMessage extends GameMessage {

    private final int fromPosition;
    private final int toPosition;
    private final PieceColor color;

    public MoveMessage(int fromPosition, int toPosition, PieceColor color) {
        this.fromPosition = fromPosition;
        this.toPosition = toPosition;
        this.color = color;
    }

    public int getToPosition() {
        return toPosition;
    }

    public int getFromPosition() {
        return fromPosition;
    }

    public PieceColor getColor() {
        return color;
    }

}
