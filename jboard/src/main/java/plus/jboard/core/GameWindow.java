package plus.jboard.core;

import plus.jboard.graphics.RenderContext;

import javax.swing.*;
import java.awt.*;

public class GameWindow {

    public RenderContext renderContext;

    public GameWindow(String title) {
        renderContext = new RenderContext();
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(720, 480));
        frame.add(renderContext);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
