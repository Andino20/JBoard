package plus.sprak.app;

import plus.jboard.core.GameObject;
import plus.jboard.math.Rectangle;
import plus.jboard.math.Vector2D;
import plus.jboard.render.RenderObject;
import plus.jboard.render.TextRenderObject;

import java.awt.Color;
import java.util.Random;

public class Die extends GameObject {

    private final Random rng;
    private final Runnable clickCallback;

    private int lastRoll;
    private Color color = Color.BLACK;

    public Die(Vector2D position, Runnable clickCallback) {
        this.clickCallback = clickCallback;
        this.rng = new Random();
        this.setPosition(position);
    }

    public void roll() {
        this.lastRoll = rng.nextInt(6) + 1;
    }

    public int getRoll() {
        return lastRoll;
    }

    public void setLastRoll(int force) {
        this.lastRoll = force;
    }

    public void setActive(boolean active) {
        this.color = active ? Color.GREEN : Color.BLACK;
    }

    @Override
    public void onMouseClick(Vector2D position) {
        clickCallback.run();
    }

    @Override
    public Rectangle getBoundingBox() {
        return new Rectangle(this.getPosition(), Vector2D.of(32, 32));
    }

    @Override
    public RenderObject toRenderObject() {
        String text = "" + lastRoll;
        return new TextRenderObject(this.getPosition().sub(Vector2D.of(0, -32)), text, color);
    }

}
