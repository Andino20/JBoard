package plus.jboard.render;

import plus.jboard.math.Vector2D;

import java.awt.Graphics2D;

public record SpriteRenderObject(Vector2D position, Sprite sprite) implements RenderObject {

    @Override
    public void draw(Graphics2D g2d) {
        if (sprite != null) {
            g2d.drawImage(sprite.getImage(), (int) position.x(), (int) position.y(), null);
        }
    }

}
