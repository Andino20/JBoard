package plus.sprak.app.messages;

/**
 * Represents a move request of a piece by transmitting its current location*/
public class TextMessage extends GameMessage {

    // Just as an example. Use primitive types or types which implement java.io.Serializable
    // otherwise we cannot parse message of this type
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

