package plus.jboard.core;

import plus.jboard.render.Drawable;

import javax.swing.*;
import java.util.List;

public class GameApplication {

    public GameApplication(String title, int width, int height, List<? extends Drawable> gameObjects) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
