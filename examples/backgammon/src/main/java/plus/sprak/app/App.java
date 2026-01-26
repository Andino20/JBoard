package plus.sprak.app;

import plus.jboard.core.GameApplication;
import plus.sprak.lobby.MainMenuScene;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        GameApplication app = new GameApplication("Backgammon", 840, 580, new MainMenuScene());
        app.run();

    }
}
