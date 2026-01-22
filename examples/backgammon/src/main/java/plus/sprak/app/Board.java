package plus.sprak.app;

import plus.jboard.core.GameApplication;
import plus.jboard.math.Vector2D;
import plus.jboard.net.NetworkEnvelope;
import plus.jboard.net.handler.MessageHandler;
import plus.sprak.app.messages.GameMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Board implements MessageHandler<GameMessage> {
    //TODO: Würfel (in dem Fall DieSelector) so animieren dass man immer sieht das gewürfelt wurde (wie bei Maedn)
    //TODO: Backgammon über das Netzwerk spielbar machen
    //TODO: Eventuell: Column oder so ähnlich als Klasse rausziehen
    //TODO: Super nice aber nicht notwendig: Rückgängigkeitsbutton bei beiden spielen der move zurück nimmt

    private static final int NUM_FIELDS = 24;
    private static final int BLACK_GOAL = -2;
    private static final int WHITE_GOAL = 25;

    private final Stack<Piece>[] fields = new Stack[NUM_FIELDS];
    private final List<Piece> pieces = new ArrayList<>();

    private final DieSelector dieSelector;

    private final Stack<Piece> blackHome = new Stack<>();
    private final Stack<Piece> whiteHome = new Stack<>();

    private final Stack<Piece> blackGoal = new Stack<>();
    private final Stack<Piece> whiteGoal = new Stack<>();

    public Board(DieSelector dieSelector) throws IOException {
        this.dieSelector = dieSelector;
        initBoard();
        GameApplication.getInstance().getMessageDispatcher().lateRegister(this);
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
            move(p, fields[column]);
            p.setMoveListener(this::triggerMove);
            pieces.add(p);
        }
    }

    public void triggerMove(Piece p) {
        int dice = this.dieSelector.getRoll();
        int change = 1; // Richtung ändert sich in Abhängigkeit von der Farbe
        if (p.getColor() == PieceColor.BLACK)
            change = -1;
        int nextPos = (p.getFieldPosition() + (dice * change));

        if (wouldMoveToGoal(p, dice)) {
            if (canMoveToGoal(p.getColor())) {
                moveInGoal(p);
            } else {
                System.out.println("Piece cannot move to goal yet!");
            }
            return;
        }

        Stack<Piece> destinedPosition = fields[nextPos];
        if (destinedPosition.isEmpty() || destinedPosition.getFirst().getColor() == p.getColor()) {
            move(p, destinedPosition); // no piece in the way, just move
        } else if (destinedPosition.getFirst().getColor() != p.getColor()) {
            if (destinedPosition.size() == 1) {//ein andersfarbiges Piece
                if (destinedPosition.getFirst().getColor() == PieceColor.WHITE) {
                    move(destinedPosition.getFirst(), whiteHome); // move other piece out of the way
                } else {
                    move(destinedPosition.getFirst(), blackHome); //move other piece out of the way
                }
                move(p, destinedPosition);
            } else {
                System.out.println("Cant move Piece to this position");
            }
        } else {
            move(p, destinedPosition);
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
    }

    public void move(Piece p, Stack<Piece> destinedPosition) {
        Stack<Piece> oldposition = getColumnOfPiece(p);

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

    @Override
    public Class<GameMessage> getAssociatedMessageType() {
        return GameMessage.class;
    }

    @Override
    public void handle(NetworkEnvelope<GameMessage> envelope) {
        // TODO: if (envelope.message() instanceof...
    }

}
