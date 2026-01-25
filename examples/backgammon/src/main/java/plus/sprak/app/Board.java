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

import static plus.sprak.app.PieceColor.WHITE;
import static plus.sprak.app.PieceColor.BLACK;

public class Board implements MessageHandler<GameMessage> {

    private static final int NUM_FIELDS = 24;
    private static final int BLACK_HOME = 24;
    private static final int BLACK_GOAL = -2;
    private static final int WHITE_HOME = -1;
    private static final int WHITE_GOAL = 25;

    private final boolean isHost;
    private final Session session;
    private boolean initialized = false;

    private final Stack<Piece>[] fields = new Stack[NUM_FIELDS];
    private final List<Piece> pieces = new ArrayList<>();

    private final DieSelector dieSelector;

    private final Stack<Piece> blackHome = new Stack<>();
    private final Stack<Piece> whiteHome = new Stack<>();

    private final Stack<Piece> blackGoal = new Stack<>();
    private final Stack<Piece> whiteGoal = new Stack<>();

    public Board(DieSelector dieSelector, boolean host, Session session) throws IOException {
        this.dieSelector = dieSelector;
        this.session = session;
        this.isHost = host;
        initBoard();
        GameApplication.getInstance().getMessageDispatcher().lateRegister(this);
    }

    private void initBoard() throws IOException {
        Arrays.setAll(fields, i -> new Stack<>());
        initStartPosition(WHITE, 0, 2);
        initStartPosition(WHITE, 11, 5);
        initStartPosition(WHITE, 18, 5);
        initStartPosition(WHITE, 16, 3);
        initStartPosition(BLACK, 5, 5);
        initStartPosition(BLACK, 7, 3);
        initStartPosition(BLACK, 12, 5);
        initStartPosition(BLACK, 23, 2);
        initialized = true;
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
        if (!isHost) {
            MoveRequest msg = new MoveRequest();
            msg.setFromPosition(clicked.getFieldPosition());
            session.broadcast(msg);
            return;
        }

        int roll = this.dieSelector.getRoll();
        Piece p = getColumnByPosition(clicked.getFieldPosition()).peek();

        if (wouldMoveToGoal(p, roll)) {
            if (canColorMoveToGoal(p.getColor())) {
                int goal = p.getColor() == WHITE ? WHITE_GOAL : BLACK_GOAL;
                move(p, goal);
            }
            return;
        }

        int direction = p.getColor() == WHITE ? 1 : -1;
        int nextPos = p.getFieldPosition() + direction * roll;

        Stack<Piece> destinedPosition = getColumnByPosition(nextPos);
        if (destinedPosition.isEmpty() || destinedPosition.getFirst().getColor() == p.getColor()) {
            move(p, nextPos);
        } else if (destinedPosition.size() == 1) {
            int home = destinedPosition.getFirst().getColor() == WHITE ? WHITE_HOME : BLACK_HOME;
            move(destinedPosition.getFirst(), home);
            move(p, nextPos);
        }
    }

    public void move(Piece p, int nextPosition) {
        int fromPosition = p.getFieldPosition();

        Stack<Piece> prevColumn = getColumnByPosition(p.getFieldPosition());
        Stack<Piece> nextColumn = getColumnByPosition(nextPosition);
        nextColumn.push(p);
        if (!prevColumn.isEmpty()) {
            prevColumn.pop();
        }

        p.setFieldPosition(nextPosition);
        updateScreenPosition(p);

        if (isHost && initialized) {
            MoveMessage msg = new MoveMessage(fromPosition, nextPosition);
            session.broadcast(msg);
        }
    }

    private Stack<Piece> getColumnByPosition(int position) {
        if (position >= 0 && position < NUM_FIELDS) {
            return fields[position];
        } else {
            return switch (position) {
                case WHITE_HOME -> whiteHome;
                case WHITE_GOAL -> whiteGoal;
                case BLACK_HOME -> blackHome;
                case BLACK_GOAL -> blackGoal;
                default -> throw new IllegalArgumentException("invalid column position " + position);
            };
        }
    }

    private boolean wouldMoveToGoal(Piece p, int roll) {
        return switch (p.getColor()) {
            case WHITE -> p.getFieldPosition() + roll >= NUM_FIELDS;
            case BLACK -> p.getFieldPosition() - roll < 0;
        } && !isInHome(p);
    }

    private boolean canColorMoveToGoal(PieceColor playerColor) {
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

    private boolean isInHome(Piece p) {
        return p.getFieldPosition() == BLACK_HOME || p.getFieldPosition() == WHITE_HOME;
    }

    private void updateScreenPosition(Piece p) {
        Stack<Piece> currentColumn = getColumnByPosition(p.getFieldPosition());

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

    @Override
    public Class<GameMessage> getAssociatedMessageType() {
        return GameMessage.class;
    }

    @Override
    public void handle(NetworkEnvelope<GameMessage> envelope) {
        if (envelope.message() instanceof MoveRequest message && isHost) {
            Piece f = getColumnByPosition(message.getFromPosition()).peek();
            triggerMove(f);
        } else if (envelope.message() instanceof MoveInGoalMessage message && !isHost) {
            Piece p = getColumnByPosition(message.getFromPosition()).peek();
            int goal = p.getColor() == WHITE ? WHITE_GOAL : BLACK_GOAL;
            move(p, goal);
        } else if (envelope.message() instanceof MoveMessage message && !isHost) {
            Piece p = getColumnByPosition(message.getFromPosition()).peek();
            int destination = message.getDestination();
            move(p, destination);
        }
    }

}
