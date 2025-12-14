package plus.jboard.core;

import plus.jboard.math.Rectangle;
import plus.jboard.math.Vector2D;
import plus.jboard.render.RenderObject;
import plus.jboard.render.Sprite;
import plus.jboard.render.SpriteRenderObject;

import java.awt.Graphics2D;

public abstract class GameObject implements RenderObject {
    private Vector2D position;
    protected Sprite sprite;

    protected GameObject() {
        position = new Vector2D(0, 0);
        sprite = null;
    }

    public void update() {
        // subclasses can choose to override this method, but they don't have to
    }

    public void onMouseClick(Vector2D position) {
        // subclasses can choose to override this method, but they don't have to
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

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
