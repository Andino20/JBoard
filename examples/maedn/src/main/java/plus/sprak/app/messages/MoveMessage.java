package plus.sprak.app.messages;

import plus.sprak.app.PieceColor;

public class MoveMessage extends GameMessage {

    private int fromPosition;
    private int toPosition;
    private PieceColor color;

    public int getToPosition() {
        return toPosition;
    }

    public void setToPosition(int toPosition) {
        this.toPosition = toPosition;
    }

    public int getFromPosition() {
        return fromPosition;
    }

    public void setFromPosition(int fromPosition) {
        this.fromPosition = fromPosition;
    }

    public PieceColor getColor() {
        return color;
    }

    public void setColor(PieceColor color) {
        this.color = color;
    }
    
}
