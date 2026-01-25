package plus.sprak.app;

import plus.jboard.core.GameApplication;
import plus.jboard.net.NetworkEnvelope;
import plus.jboard.net.handler.MessageHandler;
import plus.jboard.net.session.Session;
import plus.sprak.app.messages.GameMessage;
import plus.sprak.app.messages.MoveMessage;
import plus.sprak.app.messages.MoveRequest;

import java.io.IOException;
import java.util.*;

public class Board implements MessageHandler<GameMessage> {

    private static final int NUM_FIELDS = 40;
    private final boolean isHost;

    private final Figure[] fields = new Figure[NUM_FIELDS];
    private final Map<PieceColor, Figure[]> homes = new EnumMap<>(PieceColor.class);
    private final Map<PieceColor, Figure[]> goals = new EnumMap<>(PieceColor.class);

    private final List<Figure> figures = new ArrayList<>();
    private final Die d6;

    private final Session session;

    public Board(Die d6, boolean host, Session session) throws IOException {
        this.d6 = d6;
        this.isHost = host;
        this.session = session;
        for (PieceColor c : PieceColor.values()) {
            goals.putIfAbsent(c, new Figure[4]);
            Figure[] homeFigures = homes.computeIfAbsent(c, k -> new Figure[4]);

            for (int i = 0; i < homeFigures.length; i++) {
                Figure f = new Figure(c);
                move(f, -100); //for initialisation

                f.setMoveListener(this::triggerMove);
                figures.add(f);
            }
        }

        // Register this class as a message handler with the dispatcher
        // You can create as many message handler of different types as you want
        // Just remember to register them, or otherwise they cannot receive messages
        GameApplication.getInstance().getMessageDispatcher().lateRegister(this);
    }

    public void triggerMove(Figure f) {
        if(!isHost){
            MoveRequest msg = new MoveRequest();
            msg.setFromPosition(f.getFieldPosition());
            msg.setColor(f.getColor());
            session.broadcast(msg);
            return;
        }
        if(d6.isUsed()){
            return;
        }
        int dice = d6.getRoll();
        if (dice != 6 && f.getFieldPosition() < 0)
            return;

        if (f.getFieldPosition() > 99 && ((dice + f.getFieldPosition()) % 100 > 3 || goals.get(f.getColor()) == null))
            return;

        int nextPos = (f.getFieldPosition() + dice) % NUM_FIELDS;
        if (dice == 6 && f.getFieldPosition() < 0) {
            nextPos = getStartPositionByColor(f.getColor());
        } else if (nextPos >= getStartPositionByColor(f.getColor()) && nextPos <= getStartPositionByColor(f.getColor()) + 6 && (nextPos % 10) < dice) { //move into house
            nextPos = 100 + (f.getFieldPosition() + dice) % 10;
            if (goals.get(f.getColor())[nextPos%100] == null) { //check for space in goal, go around otherwise
                move(f, nextPos);
            } else {
                nextPos = getStartPositionByColor(f.getColor()) + nextPos%100;
            }
        } else if (f.getFieldPosition() > 99) { //move inside goal
            nextPos = f.getFieldPosition() + dice;
            if (goals.get(f.getColor())[nextPos%100] == null) {
                move(f, nextPos);
            }
            else{
                return;
            }
        }

        if (f.getFieldPosition() < 100) { //normal move, not in goal
            if (fields[nextPos] == null) {
                move(f, nextPos); // no piece in the way, just move
            } else if (fields[nextPos].getColor() != f.getColor()) {
                move(fields[nextPos], -1); // move other piece out of the way
                move(f, nextPos);
            } else {
                return;
            }
        }

        d6.use();
    }

    public void move(Figure f, int newPosition) {
        int oldPosition = f.getFieldPosition();
        int calcNewPosition = newPosition;
        if (newPosition < 0) { // move to home
            Figure[] home = homes.get(f.getColor());
            OptionalInt freeSpot = findFreeHomeSpot(f.getColor());
            int i;
            if (freeSpot.isPresent()) {
                i = freeSpot.getAsInt();
                home[i] = f;
                f.setPosition(Constants.homeToPixel.get(f.getColor()).get(i));
                calcNewPosition = i - 10; //to encode the different places in home
            }
        } else if (newPosition > 99){ // move in goal
            Figure[] goal = goals.get(f.getColor());
            int i = newPosition % 100;
            if (goal[i] != null) {
                return;
            }
            goal[i] = f;
            f.setPosition(Constants.goalToPixel.get(f.getColor()).get(i));
        }
        else { // move to field
            fields[newPosition] = f;
            f.setPosition(Constants.fieldToPixel.get(newPosition));
        }

        // Clean up behind us
        if (f.getFieldPosition() >= 0 && f.getFieldPosition() < 100) {
            this.fields[f.getFieldPosition()] = null;
        } else if (f.getFieldPosition() < 0) {
            Figure[] home = this.homes.get(f.getColor());
            for (int i = 0; i < home.length; i++) {
                if (home[i] == f) {
                    home[i] = null;
                }
            }
        } else {
            Figure[] goal = this.goals.get(f.getColor());
            for (int i = 0; i < goal.length; i++) {
                if (goal[i] == f && newPosition != i) {
                    goal[i] = null;
                }
            }
            for (int i = 0; i < NUM_FIELDS; i++) {
                if (this.fields[i] == f) {
                    this.fields[i] = null;
                }
            }
        }

        f.setFieldPosition(calcNewPosition);
        if(isHost && newPosition != -100){
            MoveMessage msg = new MoveMessage();
            msg.setFromPosition(oldPosition);
            msg.setToPosition(newPosition);
            msg.setColor(f.getColor());
            session.broadcast(msg);
        }
    }

    public List<Figure> getAllFigures() {
        return figures;
    }

    private OptionalInt findFreeHomeSpot(PieceColor color) {
        Figure[] home = homes.get(color);
        for (int i = 0; i < home.length; i++) {
            if (home[i] == null) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    private static int getStartPositionByColor(PieceColor color) {
        return switch (color) {
            case RED -> 0;
            case YELLOW -> 10;
            case BLUE -> 20;
            case GREEN -> 30;
        };
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
        if (envelope.message() instanceof MoveRequest message) {
            // handle move message (depends on if we are host or not
            if(isHost){
                Figure f;
                if(message.getFromPosition() < 0){
                    f = homes.get(message.getColor())[10+message.getFromPosition()];
                }
                else if(message.getFromPosition() > 99){
                    f = goals.get(message.getColor())[message.getFromPosition()-100];
                }
                else{
                    f = fields[message.getFromPosition()];
                }

                triggerMove(f);
            }
        } else if (envelope.message() instanceof MoveMessage message && !isHost){
                Figure f;
                if(message.getFromPosition() < 0){
                    f = homes.get(message.getColor())[10+message.getFromPosition()];
                }
                else if(message.getFromPosition() > 99){
                    f = goals.get(message.getColor())[message.getFromPosition()-100];
                }
                else{
                    f = fields[message.getFromPosition()];
                }

                move(f, message.getToPosition());
                d6.use();
            }

    }

}
