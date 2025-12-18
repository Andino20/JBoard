package plus.sprak.app;

import plus.jboard.core.GameApplication;

public class App {
    public static void main(String[] args) {
        BackgammonScene gameScene = new BackgammonScene();
        GameApplication app = new GameApplication("Backgammon", 720, 480, gameScene);
        app.run();

    }
}
