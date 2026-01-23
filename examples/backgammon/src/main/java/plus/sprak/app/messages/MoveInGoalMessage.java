package plus.sprak.app.messages;

/**
 * Represents a move request of a piece by transmitting its current location*/
public class MoveInGoalMessage extends GameMessage {

    // Just as an example. Use primitive types or types which implement java.io.Serializable
    // otherwise we cannot parse message of this type
    private int fromPosition;

    public int getFromPosition() {
        return fromPosition;
    }

    public void setFromPosition(int fromPosition) {
        this.fromPosition = fromPosition;
    }
}

