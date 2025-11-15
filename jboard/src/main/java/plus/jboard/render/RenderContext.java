package plus.jboard.render;

import java.awt.*;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;

public class RenderContext extends JPanel {
    private List<? extends Drawable> objects;

    public RenderContext() {
        this.setBackground(Color.WHITE);
        objects = new ArrayList<>();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (Drawable object : objects) {
            object.draw(g2d);
        }
    }

    public void render(List<? extends Drawable> objects) {
        this.objects = objects;
        this.repaint();
    }
}

