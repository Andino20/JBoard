package plus.jboard.core;

import lombok.Getter;
import lombok.Setter;
import plus.jboard.math.Rectangle;
import plus.jboard.math.Vector2D;
import plus.jboard.render.RenderObject;
import plus.jboard.render.Sprite;
import plus.jboard.render.SpriteRenderObject;

import java.awt.Graphics2D;

public class GameObject implements RenderObject {

    @Setter
    @Getter
    private Vector2D position;

    protected Sprite sprite;

    protected GameObject() {
        position = new Vector2D(0, 0);
        sprite = null;
    }

    public void update() {}

    public void onMouseClick(Vector2D position) {}

    public Rectangle getBoundingBox() {
        if (sprite == null) {
            return new Rectangle(new Vector2D(0, 0), new Vector2D(0, 0));
        }
        return new Rectangle(this.getPosition(), new Vector2D(sprite.getWidth(), sprite.getHeight()));
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (sprite != null) {
            g2d.drawImage(sprite.getImage(), (int) position.x(), (int) position.y(), null);
        }
    }

    public RenderObject toRenderObject() {
        return new SpriteRenderObject(Vector2D.copyOf(position), sprite);
    }

}
