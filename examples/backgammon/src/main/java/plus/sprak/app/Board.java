package plus.sprak.app;

import plus.jboard.math.Vector2D;

import java.io.IOException;
import java.util.*;

public class Board {
    //Todo: vielleicht text im window zeichen lassen
    private static final int NUM_FIELDS = 24;
    //todo: kann fields array 2 werte halten? also ein piece und wieviele pices oder halt meherer pieces?
    private final Stack<Piece>[] fields = new Stack[NUM_FIELDS];
    private final Map<PieceColor, Piece[]> homes = new EnumMap<>(PieceColor.class);
    //private final Map<pieceColor, Piece[]> goals = new EnumMap<>(pieceColor.class);

    private final Stack<Piece> blackHome = new Stack<>();
    private final Stack<Piece> whiteHome = new Stack<>();
    private final Stack<Piece> blackGoal = new Stack<>();
    private final Stack<Piece> whiteGoal = new Stack<>();
    private final List<Piece> pieces = new ArrayList<>();
    private final Die d6;

    public Board(Die d6) throws IOException {
        this.d6 = d6;
        for (int i = 0; i < NUM_FIELDS; i++) {
            fields[i] = new Stack<Piece>();
        }
        //goals.putIfAbsent(c, new Piece[4]);
        //Piece[] homePieces = homes.computeIfAbsent(c, k -> new Piece[4]);

        //initStartPosition(PieceColor.WHITE, 19, 5);
        //initStartPosition(PieceColor.WHITE, 20, 5);
        //initStartPosition(PieceColor.WHITE, 18, 5);



        initStartPosition(PieceColor.WHITE, 0, 2);
        initStartPosition(PieceColor.WHITE, 11, 5);
        initStartPosition(PieceColor.WHITE, 18, 5);
        initStartPosition(PieceColor.WHITE, 16, 3);
        initStartPosition(PieceColor.BLACK, 5, 5);
        initStartPosition(PieceColor.BLACK, 7, 3);
        initStartPosition(PieceColor.BLACK, 12, 5);
        initStartPosition(PieceColor.BLACK, 23, 2);
        // sp = 4 (mit 5 pieces), 6 (mit 3 pieces)

        //board liste initialisieren
        for (int i = 0; i < NUM_FIELDS; i++) {
            fields[i] = new Stack<>();
        }
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
        int dice = d6.getRoll();
        int change = 1; // Richtung ändert sich in Abhängigkeit von der Farbe
        if (p.getColor() == PieceColor.BLACK)
            change = -1;
        int nextPos = (p.getFieldPosition() + (dice * change)); //TODO: % NUM_FIELDS Odulu bei Minus überprüfen

        //Alle im Ziel?
        if (nextPos >= NUM_FIELDS) {
            int piececInEndfield = 0;
            int startfield = 0;
            int endfield = 24;
            switch (p.getColor()) {
                case WHITE -> {
                    startfield = 18;
                    endfield = 25;
                }
                case BLACK -> {
                    startfield = -2;
                    endfield = 5;
                }
            }

            for (Piece piece : pieces) {
                if (startfield <= piece.getFieldPosition() && piece.getFieldPosition() <= endfield) {
                    piececInEndfield++;
                }
            }
            if (piececInEndfield == 15) {
                moveInGoal(p);
            } else {
                System.out.println("Cant move Piece to this position");
                return;
            }

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
                return;
            }

        } else {
            move(p, destinedPosition);
        }
    }

    //neu
    public void moveInGoal(Piece p) {
        Stack<Piece> old = getColumnOfPiece(p);
        Stack<Piece> dest = null;
        switch (p.getColor()) {
            case WHITE -> {
                p.setFieldPosition(-2);
                dest = whiteGoal;
            }
            case BLACK -> {
                p.setFieldPosition(25);
                dest = blackGoal;
            }
        }
        dest.push(p);
        if (old.isEmpty()) {
            old.pop();
        }//Todo: falls fehler,d aufpassen ob wir mehr als höchtes element bewegen
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
                if (fields[i] == destinedPosition) {//eventuell equals statt ==
                    p.setFieldPosition(i);
                    break;
                }
            }
        }

        destinedPosition.push(p);
        if (!oldposition.isEmpty()) {
            oldposition.pop();
        }//Todo: falls fehler,d aufpassen ob wir mehr als höchtes element bewegen
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

    private void updateScreenPosition(Piece p) {
        Stack<Piece> currentColumn;
        switch (p.getFieldPosition()) {
            case -1 -> currentColumn = whiteHome;
            case 24 -> currentColumn = blackHome;
            default -> currentColumn = fields[p.getFieldPosition()];
        }

        Vector2D columnBase = Constants.columnPositionToPixel.get(p.getFieldPosition());
        int columnOffset = Math.max(0, currentColumn.size());
        //int offsetMod
        if (p.getFieldPosition() < 12) {
            columnOffset -= 1; //Todo: position an seiten?
        } else {
            columnOffset *= -1;
        }
        p.setPosition(columnBase.add(Vector2D.of(0, 40).scale(columnOffset)));
    }

    public List<Piece> getAllPieces() {
        return pieces;
    }

    //todo: umändern weil home jetze einfach eine art liste? der besser gesagt nur positionen ändern oder?
    private OptionalInt findFreeHomeSpot(PieceColor color) {
        Piece[] home = homes.get(color);
        for (int i = 0; i < home.length; i++) {
            if (home[i] == null) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    //Todo: entfernen
    private static int[] getStartPositionsByColor(PieceColor color) {
        return switch (color) {
            case WHITE -> new int[]{0, 11, 2, 4};
            case BLACK -> new int[]{23, 18, 5, 7};
        };
    }
}
