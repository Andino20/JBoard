package plus.sprak.app.messages;

import plus.sprak.app.PieceColor;

public class MoveRequest extends GameMessage {

    private final int fromPosition;
    private final PieceColor color;

    public MoveRequest(int fromPosition, PieceColor color) {
        this.fromPosition = fromPosition;
        this.color = color;
    }

    public int getFromPosition() {
        return fromPosition;
    }

    public PieceColor getColor() {
        return color;
    }

}
