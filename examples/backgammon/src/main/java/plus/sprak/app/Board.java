package plus.sprak.app;

import java.io.IOException;
import java.util.*;

public class Board {

    private static final int NUM_FIELDS = 24;
    //todo: kann fields array 2 werte halten? also ein piece und wieviele pices oder halt meherer pieces?
    private final List<Piece>[] fields = new List[NUM_FIELDS];//
    private final Map<PieceColor, Piece[]> homes = new EnumMap<>(PieceColor.class);
    //private final Map<pieceColor, Piece[]> goals = new EnumMap<>(pieceColor.class);

    private final List<Piece> pieces = new ArrayList<>();
    private final Die d6;

    public Board(Die d6) throws IOException {
        this.d6 = d6;
        for (PieceColor c : PieceColor.values()) {
            //goals.putIfAbsent(c, new Piece[4]);
            //Piece[] homePieces = homes.computeIfAbsent(c, k -> new Piece[4]);
            int[] sp = getStartPositionsByColor(c);
            for (int pos : sp) {
                Piece p = new Piece(c);
                move(p, pos);

                p.setMoveListener(this::triggerMove);
                pieces.add(p);
            }

        }
        for (int i = 0; i < NUM_FIELDS; i++) {
            fields[i] = new ArrayList<>();
        }
    }

    public void triggerMove(Piece p) {
        int dice = d6.getRoll();
        int change = 1; // Richtung ändert sich in Abhängigkeit von der Farbe
        if (p.getColor() == PieceColor.BLACK)
            change = -1;
        int nextPos = (p.getFieldPosition() + dice*change) % NUM_FIELDS;

        if (fields[nextPos] == null ||fields[nextPos].getFirst().getColor() == p.getColor()) {
            move(p, nextPos); // no piece in the way, just move
        } else if (fields[nextPos].getFirst().getColor() != p.getColor()) {
            move(fields[nextPos].getFirst(), -1); // move other piece out of the way, nur wenn es nur ein piece ist
            move(p, nextPos);
        }
    }
//todo: überarbeiten weil meherer pieces an gleicher Psition sein können
    public void move(Piece p, int newPosition) {
        if (newPosition < 0) { // move to home
            Piece[] home = homes.get(p.getColor());
            findFreeHomeSpot(p.getColor()).ifPresent(i -> {
                home[i] = p;
                p.setPosition(Constants.homeToPixel.get(p.getColor()).get(i));
            });
        } else { // move to field
            fields[newPosition].add(p);
            p.setPosition(Constants.fieldToPixel.get(newPosition));
        }

        // Clean up behind us todo: machen
        if (p.getFieldPosition() >= 0) {
            this.fields[p.getFieldPosition()] = null;
        } else {
            Piece[] home = this.homes.get(p.getColor());
            for (int i = 0; i < home.length; i++) {
                if (home[i] == p) {
                    home[i] = null;
                }
            }
        }

        p.setFieldPosition(newPosition);
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

    private static int [] getStartPositionsByColor(PieceColor color) {
        return switch (color) {
            case WHITE -> new int[]{0, 1,2, 4};
            case BLACK -> new int[] {24,19,3, 7};
        };
    }
}
