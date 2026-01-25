package plus.sprak.app.messages;

public class MoveMessage extends GameMessage {

    private final int fromPosition;
    private final int destination;

    public MoveMessage(int from, int to) {
        this.fromPosition = from;
        this.destination = to;
    }

    public int getFromPosition() {
        return fromPosition;
    }

    public int getDestination() {
        return destination;
    }

}


