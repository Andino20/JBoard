package plus.sprak.app.messages;

/**
 * Represents a dice roll*/
public class DiceRoll extends GameMessage {

    // Just as an example. Use primitive types or types which implement java.io.Serializable
    // otherwise we cannot parse message of this type
    private int roll;

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }
}

