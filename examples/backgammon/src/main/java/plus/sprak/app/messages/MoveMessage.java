package plus.sprak.app.messages;

public class MoveMessage extends GameMessage {

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


