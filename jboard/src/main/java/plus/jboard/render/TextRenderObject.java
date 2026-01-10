package plus.jboard.render;

import plus.jboard.math.Vector2D;

import java.awt.*;

public record TextRenderObject(Vector2D position, String text, Color color) implements RenderObject {

    @Override
    public void draw(Graphics2D g2d) {
        if (position != null && text != null) {
            Font f = g2d.getFont();
            g2d.setFont(new Font("Arial", Font.BOLD, 32));
            g2d.setColor(color != null ? color : Color.BLACK);
            g2d.drawString(text, (int) position.x(), (int) position.y());
            g2d.setFont(f);
        }
    }
}
