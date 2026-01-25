package plus.sprak.app;

import plus.jboard.core.GameApplication;
import plus.jboard.core.GameObject;
import plus.jboard.math.Rectangle;
import plus.jboard.math.Vector2D;
import plus.jboard.net.NetworkEnvelope;
import plus.jboard.net.handler.MessageHandler;
import plus.jboard.net.session.Session;
import plus.jboard.render.RenderObject;
import plus.jboard.render.TextRenderObject;
import plus.sprak.app.messages.*;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Die extends GameObject implements MessageHandler<GameMessage> {

    private final Random rng;
    private int lastRoll = 6;
    private boolean used = false;
    private final boolean isHost;
    private final Session session;

    public Die(boolean host, Session session) {
        this.isHost = host;
        this.session = session;
        this.setPosition(Vector2D.of(150, 20));
        rng = new Random();

        GameApplication.getInstance().getMessageDispatcher().lateRegister(this);
    }

    @Override
    public void onMouseClick(Vector2D position) {
        if (this.lastRoll == 0) {
            return;
        }
        if (!isHost) {
            DiceRollRequest msg = new DiceRollRequest();
            session.broadcast(msg);
            return;
        }
        this.lastRoll = 0;
        Timer timer = new Timer(200, e -> roll(0));
        timer.setRepeats(false);
        timer.start();
    }

    public void roll(int roll) {
        if (roll == 0) {
            this.lastRoll = rng.nextInt(6) + 1;
        } else {
            this.lastRoll = roll;
        }
        this.used = false;

        if (isHost) {
            DiceRoll msg = new DiceRoll();
            msg.setRoll(lastRoll);
            session.broadcast(msg);
        }
    }

    public void use() {
        this.used = true;
    }

    public boolean isUsed() {
        return this.used;
    }

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

    @Override
    public Class<GameMessage> getAssociatedMessageType() {
        return GameMessage.class;
    }

    @Override
    public void handle(NetworkEnvelope<GameMessage> envelope) {
        if (envelope.message() instanceof DiceRollRequest && isHost) {
            roll(0);
        } else if (envelope.message() instanceof DiceRoll message && !isHost) {
            this.lastRoll = 0;
            Timer timer = new Timer(200, e -> roll(message.getRoll()));
            timer.setRepeats(false);
            timer.start();
        }

    }

}
