package plus.jboard.core;

public class GameApplication {

    private String title;
    private GameWindow window;

    public GameApplication(String title) {
        this.title = title;
        window = new GameWindow(title);
    }

    public void run() {
    }

    public void addPiece(Piece p) {
        window.renderContext.addPiece(p);
    }

}
