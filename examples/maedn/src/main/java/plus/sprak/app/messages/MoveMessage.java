package plus.sprak.app.messages;

/**
 * Represents a move request of a piece from one position on the field to another.
 * E.g. Piece at position 30 -> 36
 * E.g. Piece at position -1 (Home) -> 10 (starting position)
 * E.g. Piece at position 30 -> -1 (will need to check what color)
 * THIS IS JUST AN EXAMPLE. It could make more sense to give each piece an ID and send (ID, newPosition) instead.
 */
public class MoveMessage extends GameMessage {

    // Just as an example. Use primitive types or types which implement java.io.Serializable
    // otherwise we cannot parse message of this type
    private int fromPosition;
    private int toPosition;

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
}
