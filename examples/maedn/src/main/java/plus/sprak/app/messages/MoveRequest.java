package plus.sprak.app.messages;

import plus.sprak.app.PieceColor;

/**
 * Represents a move request of a piece by transmitting its current location*/
public class MoveRequest extends GameMessage {

    // Just as an example. Use primitive types or types which implement java.io.Serializable
    // otherwise we cannot parse message of this type
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
