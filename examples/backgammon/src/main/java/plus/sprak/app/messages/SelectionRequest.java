package plus.sprak.app.messages;

/**
 * Represents a move request of a piece by transmitting its current location*/
public class SelectionRequest extends GameMessage {

    // Just as an example. Use primitive types or types which implement java.io.Serializable
    // otherwise we cannot parse message of this type
    private int selection;

    public int getSelection() {
        return selection;
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }
}

