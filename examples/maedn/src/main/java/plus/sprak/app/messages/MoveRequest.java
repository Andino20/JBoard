package plus.sprak.app.messages;

import plus.sprak.app.PieceColor;

public class MoveRequest extends GameMessage {

    private int fromPosition;
    private PieceColor color;

    public int getFromPosition() {
        return fromPosition;
    }

    public PieceColor getColor() {
        return color;
    }

    public void setFromPosition(int fromPosition) {
        this.fromPosition = fromPosition;
    }

    public void setColor(PieceColor color) {
        this.color = color;
    }
    
}
