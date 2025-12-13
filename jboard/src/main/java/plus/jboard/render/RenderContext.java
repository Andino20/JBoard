package plus.jboard.render;

import plus.jboard.core.GameObject;

import java.awt.*;
import javax.swing.JPanel;
import java.util.Iterator;

public class RenderContext extends JPanel {
    private Iterator<GameObject> renderObjects;

    public RenderContext() {
        this.setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (renderObjects == null)
            return;

        while (renderObjects.hasNext()) {
            renderObjects.next().draw(g2d);
        }

        renderObjects = null;
    }

    public void render(Iterator<GameObject> renderObjects) {
        this.renderObjects = renderObjects;
        this.repaint();
    }
}

