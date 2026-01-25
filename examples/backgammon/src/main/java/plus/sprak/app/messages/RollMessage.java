package plus.sprak.app.messages;

public class RollMessage extends GameMessage {

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


