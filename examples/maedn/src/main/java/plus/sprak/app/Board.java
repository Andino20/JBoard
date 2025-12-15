package plus.sprak.app;

import java.io.IOException;
import java.util.*;

public class Board {

    private static final int NUM_FIELDS = 40;

    private final Figure[] fields = new Figure[NUM_FIELDS];
    private final Map<PieceColor, Figure[]> homes = new EnumMap<>(PieceColor.class);
    private final Map<PieceColor, Figure[]> goals = new EnumMap<>(PieceColor.class);

    private final List<Figure> figures = new ArrayList<>();
    private final Die d6;

    public Board(Die d6) throws IOException {
        this.d6 = d6;
        for (PieceColor c : PieceColor.values()) {
            goals.putIfAbsent(c, new Figure[4]);
            Figure[] homeFigures = homes.computeIfAbsent(c, k -> new Figure[4]);

            for (int i = 0; i < homeFigures.length; i++) {
                Figure f = new Figure(c);
                move(f, -1);

                f.setMoveListener(this::triggerMove);
                figures.add(f);
            }
        }
    }

    public void triggerMove(Figure f) {
        int dice = d6.getRoll();
        if (dice != 6 && f.getFieldPosition() < 0)
            return;

        int nextPos;
        if (dice == 6 && f.getFieldPosition() < 0) {
            nextPos = getStartPositionByColor(f.getColor());
        } else {
            nextPos = (f.getFieldPosition() + dice) % NUM_FIELDS;
        }

        if (fields[nextPos] == null) {
            move(f, nextPos); // no piece in the way, just move
        } else if (fields[nextPos].getColor() != f.getColor()) {
            move(fields[nextPos], -1); // move other piece out of the way
            move(f, nextPos);
        }
    }

    public void move(Figure f, int newPosition) {
        if (newPosition < 0) { // move to home
            Figure[] home = homes.get(f.getColor());
            findFreeHomeSpot(f.getColor()).ifPresent(i -> {
                home[i] = f;
                f.setPosition(Constants.homeToPixel.get(f.getColor()).get(i));
            });
        } else { // move to field
            fields[newPosition] = f;
            f.setPosition(Constants.fieldToPixel.get(newPosition));
        }

        // Clean up behind us
        if (f.getFieldPosition() >= 0) {
            this.fields[f.getFieldPosition()] = null;
        } else {
            Figure[] home = this.homes.get(f.getColor());
            for (int i = 0; i < home.length; i++) {
                if (home[i] == f) {
                    home[i] = null;
                }
            }
        }

        f.setFieldPosition(newPosition);
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
}
