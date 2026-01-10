package plus.sprak.app;

import plus.jboard.core.GameObject;
import plus.jboard.math.Rectangle;
import plus.jboard.math.Vector2D;
import plus.jboard.render.RenderObject;
import plus.jboard.render.TextRenderObject;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Die extends GameObject {

    private Random rng;
    private int lastRoll = 6;
    private boolean used = false;

    public Die() {
        this.setPosition(Vector2D.of(150, 20));
        rng = new Random();
    }

    public Die(Random rng) {
        this();
        this.rng = rng;
    }

    @Override
    public void onMouseClick(Vector2D position) {
        if(this.lastRoll == 0) {
            return;
        }
        this.lastRoll = 0;
        Timer timer = new Timer(200, e -> roll());
        timer.setRepeats(false);
        timer.start();
    }

    public void roll() {
        this.lastRoll = rng.nextInt(6) + 1;
        this.used = false;
    }

    public void use() {this.used = true;}

    public boolean isUsed() {return this.used;}

    public int getRoll() {
        return lastRoll;
    }

    @Override
    public Rectangle getBoundingBox() {
        return new Rectangle(this.getPosition(), Vector2D.of(32, 32));
    }

    @Override
    public RenderObject toRenderObject() {
        if (used) {
            return new TextRenderObject(this.getPosition().sub(Vector2D.of(0, -32)), "" + lastRoll, Color.RED);
        }
        return new TextRenderObject(this.getPosition().sub(Vector2D.of(0, -32)), "" + lastRoll, Color.BLACK);
    }

}
