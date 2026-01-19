package plus.sprak.app;

import plus.jboard.core.GameApplication;
import plus.sprak.scenes.DefaultScene;
import plus.sprak.scenes.MainMenuScene;

public class App {

    public static void main(String[] args) {
        GameApplication app = new GameApplication("Networking Example", 720, 480, new DefaultScene());
        MainMenuScene mainMenu = new MainMenuScene(new DefaultScene());
        app.switchScenes(mainMenu);
        app.run();
    }

}
