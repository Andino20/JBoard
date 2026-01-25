package plus.sprak.app.messages;

public class MoveRequest extends GameMessage {

    private int fromPosition;

    public int getFromPosition() {
        return fromPosition;
    }

    public void setFromPosition(int fromPosition) {
        this.fromPosition = fromPosition;
    }

}

