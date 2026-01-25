package plus.sprak.app.messages;

public class SelectionRequest extends GameMessage {

    private int selection;

    public int getSelection() {
        return selection;
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }

}

