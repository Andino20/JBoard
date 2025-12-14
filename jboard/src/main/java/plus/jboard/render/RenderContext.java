package plus.jboard.render;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

public class RenderContext extends JPanel {
    private List<RenderObject> renderObjects;

    public RenderContext() {
        this.setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (renderObjects == null)
            return;

        for (RenderObject renderObject : renderObjects) {
            renderObject.draw(g2d);
        }

        renderObjects = null;
    }

    public void render(List<RenderObject> renderObjects) {
        this.renderObjects = renderObjects;
        this.repaint();
    }
}

