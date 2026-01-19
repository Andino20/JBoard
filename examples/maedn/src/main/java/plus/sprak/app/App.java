package plus.sprak.app;

import plus.jboard.core.GameApplication;
import plus.sprak.lobby.MainMenuScene;

import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException {
        MainMenuScene menu = new MainMenuScene();
        GameApplication app = new GameApplication("Mensch-ärgere-dich-nicht!", 720, 480, menu);
        app.run();
    }

}