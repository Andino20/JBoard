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

    private Random rng;
    private int lastRoll = 6;
    private boolean used = false;
    private final boolean HOST;
    private final Session session;

    public Die(boolean host, Session session) {
        this.HOST = host;
        this.session = session;
        this.setPosition(Vector2D.of(150, 20));
        rng = new Random();

        // Register this class as a message handler with the dispatcher
        // You can create as many message handler of different types as you want
        // Just remember to register them, or otherwise they cannot receive messages
        GameApplication.getInstance().getMessageDispatcher().lateRegister(this);
    }

    @Override
    public void onMouseClick(Vector2D position) {
        if(this.lastRoll == 0) {
            return;
        }
        if(!HOST){
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
        if(roll == 0) {
            this.lastRoll = rng.nextInt(6) + 1;
        }
        else{
            this.lastRoll = roll;
        }
        this.used = false;

        if(HOST){
            DiceRoll msg = new DiceRoll();
            msg.setRoll(lastRoll);
            session.broadcast(msg);
        }
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

    // We tell the message dispatcher what type of message we are interested in
    @Override
    public Class<GameMessage> getAssociatedMessageType() {
        return GameMessage.class;
    }

    // We receive message of our type of interest
    // Envelopes also contain the connection from where the message came, so you could theoretically reply
    // to the sender
    @Override
    public void handle(NetworkEnvelope<GameMessage> envelope) {
        if (envelope.message() instanceof DiceRollRequest message) {
            if(HOST){
                roll(0);
            }
        } else if (envelope.message() instanceof DiceRoll message) {
            if(!HOST){
                this.lastRoll = 0;
                Timer timer = new Timer(200, e -> roll(message.getRoll()));
                timer.setRepeats(false);
                timer.start();
            }
        }
    }

}
