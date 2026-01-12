package plus.sprak.app;

import plus.jboard.core.GameApplication;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        BackgammonScene gameScene = new BackgammonScene();
        GameApplication app = new GameApplication("Backgammon", 840, 560, gameScene);
        app.run();

    }
}
