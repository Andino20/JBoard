package plus.sprak.app;

import plus.jboard.core.GameApplication;

import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException {
        MaednScene gameScene = new MaednScene();
        GameApplication app = new GameApplication("Mensch-ärgere-dich-nicht!", 720, 480, gameScene);
        app.run();
    }
}