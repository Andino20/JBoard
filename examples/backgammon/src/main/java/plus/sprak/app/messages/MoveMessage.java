package plus.sprak.app.messages;

/**
 * Represents a move request of a piece by transmitting its current location*/
public class MoveMessage extends GameMessage {

    // Just as an example. Use primitive types or types which implement java.io.Serializable
    // otherwise we cannot parse message of this type
    private int fromPosition;
    private int destination;

    public int getFromPosition() {
        return fromPosition;
    }
    public int getDestination() {
        return destination;
    }

    public void setFromPosition(int fromPosition) {
        this.fromPosition = fromPosition;
    }

    public void setDestination(int destination) {
            this.destination = destination;
    }
}


