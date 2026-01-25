package plus.sprak.app;

import plus.jboard.core.GameApplication;
import plus.jboard.math.Vector2D;
import plus.jboard.net.NetworkEnvelope;
import plus.jboard.net.handler.MessageHandler;
import plus.jboard.net.session.Session;
import plus.sprak.app.messages.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Board implements MessageHandler<GameMessage> {

    private static final int NUM_FIELDS = 24;
    private static final int BLACK_GOAL = -2;
    private static final int WHITE_GOAL = 25;
    private final boolean HOST;
    private final Session session;
    private boolean initialisation = false;

    private final Stack<Piece>[] fields = new Stack[NUM_FIELDS];
    private final List<Piece> pieces = new ArrayList<>();

    private final DieSelector dieSelector;

    private final Stack<Piece> blackHome = new Stack<>();
    private final Stack<Piece> whiteHome = new Stack<>();

    private final Stack<Piece> blackGoal = new Stack<>();
    private final Stack<Piece> whiteGoal = new Stack<>();

    public Board(DieSelector dieSelector, boolean host, Session session) throws IOException {
        setInitialisation(true);
        this.dieSelector = dieSelector;
        this.session = session;
        this.HOST = host;
        initBoard();
        GameApplication.getInstance().getMessageDispatcher().lateRegister(this);
        setInitialisation(false);
    }

    private void initBoard() throws IOException {
        Arrays.setAll(fields, i -> new Stack<>());
        initStartPosition(PieceColor.WHITE, 0, 2);
        initStartPosition(PieceColor.WHITE, 11, 5);
        initStartPosition(PieceColor.WHITE, 18, 5);
        initStartPosition(PieceColor.WHITE, 16, 3);
        initStartPosition(PieceColor.BLACK, 5, 5);
        initStartPosition(PieceColor.BLACK, 7, 3);
        initStartPosition(PieceColor.BLACK, 12, 5);
        initStartPosition(PieceColor.BLACK, 23, 2);
    }

    private void initStartPosition(PieceColor c, int column, int amount) throws IOException {
        for (int i = 0; i < amount; i++) {
            Piece p = new Piece(c);
            move(p, column);
            p.setMoveListener(this::triggerMove);
            pieces.add(p);
        }
    }

    public void triggerMove(Piece clicked) {
        if (!HOST) {
            MoveRequest msg = new MoveRequest();
            msg.setFromPosition(clicked.getFieldPosition());
            session.broadcast(msg);
            return;
        }
        int dice = this.dieSelector.getRoll();
        Piece p = fields[clicked.getFieldPosition()].peek();
        int change = 1;
        if (p.getColor() == PieceColor.BLACK)
            change = -1;
        int nextPos = (p.getFieldPosition() + (dice * change));

        if (wouldMoveToGoal(p, dice)) {
            if (canMoveToGoal(p.getColor())) {
                moveInGoal(p);
            } else {
                TextMessage msg = new TextMessage();
                msg.setText("Piece cannot move to goal yet!");
                session.broadcast(msg);
                System.out.println("Piece cannot move to goal yet!");
            }
            return;
        }

        Stack<Piece> destinedPosition = fields[nextPos];
        if (destinedPosition.isEmpty() || destinedPosition.getFirst().getColor() == p.getColor()) {
            move(p, nextPos); // no piece in the way, just move
        } else if (destinedPosition.getFirst().getColor() != p.getColor()) {
            if (destinedPosition.size() == 1) {//ein andersfarbiges Piece
                if (destinedPosition.getFirst().getColor() == PieceColor.WHITE) {
                    move(destinedPosition.getFirst(), -10); // move other piece out of the way
                } else {
                    move(destinedPosition.getFirst(), -20); //move other piece out of the way
                }
                move(p, nextPos);
            } else {
                TextMessage msg = new TextMessage();
                msg.setText("Piece cannot move to goal yet!");
                session.broadcast(msg);
                System.out.println("Cant move Piece to this position");
            }
        } else {
            move(p, nextPos);
        }
    }

    public void moveInGoal(Piece p) {
        Stack<Piece> old = getColumnOfPiece(p);
        Stack<Piece> dest;
        if (p.getColor() == PieceColor.WHITE) {
            p.setFieldPosition(25);
            dest = whiteGoal;
        } else {
            p.setFieldPosition(-2);
            dest = blackGoal;
        }
        dest.push(p);
        old.pop();
        updateScreenPosition(p);

        if (HOST) {
            MoveInGoalMessage msg = new MoveInGoalMessage();
            msg.setFromPosition(p.getFieldPosition());
            session.broadcast(msg);
        }
    }

    public void move(Piece p, int destinedFieldNum) {
        int fromPosition = p.getFieldPosition();
        Stack<Piece> oldposition = getColumnOfPiece(p);
        Stack<Piece> destinedPosition = null;
        if (destinedFieldNum == -10) {
            destinedPosition = whiteHome;
        } else if (destinedFieldNum == -20) {
            destinedPosition = blackHome;
        } else {
            destinedPosition = fields[destinedFieldNum];
        }
        if (destinedPosition == whiteHome || destinedPosition == blackHome) {
            switch (p.getColor()) {
                case WHITE -> p.setFieldPosition(-1);
                case BLACK -> p.setFieldPosition(24);
            }
        } else {
            for (int i = 0; i < NUM_FIELDS; i++) {
                if (fields[i] == destinedPosition) {
                    p.setFieldPosition(i);
                    break;
                }
            }
        }

        destinedPosition.push(p);
        if (!oldposition.isEmpty()) {
            oldposition.pop();
        }
        updateScreenPosition(p);
        if (HOST && !getInitialisation()) {
            MoveMessage msg = new MoveMessage();
            msg.setFromPosition(fromPosition);
            msg.setDestination(destinedFieldNum);
            session.broadcast(msg);
        }
    }

    private Stack<Piece> getColumnOfPiece(Piece p) {
        if (p.getFieldPosition() >= 0 && p.getFieldPosition() < NUM_FIELDS) {
            return fields[p.getFieldPosition()];
        } else {
            return switch (p.getColor()) {
                case WHITE -> whiteHome;
                case BLACK -> blackHome;
            };
        }
    }

    private boolean wouldMoveToGoal(Piece p, int roll) {
        return switch (p.getColor()) {
            case WHITE -> p.getFieldPosition() + roll >= NUM_FIELDS;
            case BLACK -> p.getFieldPosition() - roll < 0;
        };
    }

    private boolean canMoveToGoal(PieceColor playerColor) {
        return pieces.stream()
                .filter(p -> p.getColor() == playerColor)
                .allMatch(p -> isInLastQuarter(p) || isInGoal(p));
    }

    private boolean isInLastQuarter(Piece p) {
        int pos = p.getFieldPosition();
        return switch (p.getColor()) {
            case WHITE -> 18 <= pos && pos <= 23;
            case BLACK -> 0 <= pos && pos <= 5;
        };
    }

    private boolean isInGoal(Piece p) {
        return p.getFieldPosition() == BLACK_GOAL || p.getFieldPosition() == WHITE_GOAL;
    }

    private void updateScreenPosition(Piece p) {
        Stack<Piece> currentColumn = switch (p.getFieldPosition()) {
            case -1 -> whiteHome;
            case 24 -> blackHome;
            case BLACK_GOAL -> blackGoal;
            case WHITE_GOAL -> whiteGoal;
            default -> fields[p.getFieldPosition()];
        };

        Vector2D columnBase = Constants.columnPositionToPixel.get(p.getFieldPosition()).add(Vector2D.of(4, 0));
        int columnOffset = currentColumn.size();
        if (p.getFieldPosition() < 12) {
            columnOffset -= 1;
        } else {
            columnOffset *= -1;
        }
        p.setPosition(columnBase.add(Vector2D.of(0, 40).scale(columnOffset)));
    }

    public List<Piece> getAllPieces() {
        return pieces;
    }

    private boolean getInitialisation() {
        return initialisation;
    }

    private void setInitialisation(boolean value) {
        initialisation = value;
    }

    @Override
    public Class<GameMessage> getAssociatedMessageType() {
        return GameMessage.class;
    }

    @Override
    public void handle(NetworkEnvelope<GameMessage> envelope) {
        if (envelope.message() instanceof MoveRequest message) {
            if (HOST) {
                Piece f = fields[message.getFromPosition()].peek(); //inefficient to always peek, but too lazy to change signatures
                triggerMove(f);
            }
        } else if (envelope.message() instanceof TextMessage message) {
            if (!HOST) {
                System.out.println(message.getText());
            }
        } else if (envelope.message() instanceof MoveInGoalMessage message) {
            if (!HOST) {
                Piece p = fields[message.getFromPosition()].peek(); //inefficient to always peek, but too lazy to change signatures;
                moveInGoal(p);
            }
        } else if (envelope.message() instanceof MoveMessage message) {
            if (!HOST) {
                Piece p = fields[message.getFromPosition()].peek(); //inefficient to always peek, but too lazy to change signatures;
                int destination = message.getDestination();
                move(p, destination);
            }
        }
    }

}
