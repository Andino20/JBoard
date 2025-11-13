package plus.sprak.app;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class App {

    public static class RenderContext extends JPanel {

        public RenderContext() {
            this.setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
        }

    }

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Mensch-ärgere-dich-nicht!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(720, 480);
        frame.setLocationRelativeTo(null);

        RenderContext renderContext = new RenderContext();
        frame.add(renderContext);

        frame.setVisible(true);

        BufferedImage piece = ImageIO.read(new File(Path.of("src", "main", "resources", "piece.png").toUri()));
        for(int i = 0; i < 10; i++) {

        }
    }
}