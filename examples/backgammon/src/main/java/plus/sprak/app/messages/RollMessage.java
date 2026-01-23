package plus.sprak.app.messages;

/**
 * Represents a move request of a piece by transmitting its current location*/
public class RollMessage extends GameMessage {

    // Just as an example. Use primitive types or types which implement java.io.Serializable
    // otherwise we cannot parse message of this type
    private int roll0;
    private int roll1;

    public int getRoll0() {
        return roll0;
    }
    public int getRoll1() {
        return roll1;
    }

    public void setRoll0(int roll0) {
        this.roll0 = roll0;
    }
    public void setRoll1(int roll1) {
        this.roll1 = roll1;
    }
}


