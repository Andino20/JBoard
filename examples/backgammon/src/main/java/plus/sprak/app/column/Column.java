package plus.sprak.app.column;

import plus.jboard.math.Vector2D;
import plus.sprak.app.Constants;
import plus.sprak.app.Piece;
import plus.sprak.app.PieceColor;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;

public class Column {

    private final Deque<Piece> stack = new LinkedList<>();
    private final int fieldPosition;

    public Column(int fieldPosition) {
        this.fieldPosition = fieldPosition;
    }

    /**
     * Pushes a {@code Piece} onto this column if the column is empty or already contains
     * pieces of the same color.
     * <p>
     * If the column contains exactly one {@code Piece} of the opposite color, that piece
     * is removed ("kicked off") and returned. After the operation, the column adopts the
     * color of the newly pushed piece.
     *
     * @param p the {@code Piece} to push onto the column
     * @return an {@code Optional} containing the kicked-off {@code Piece}, or
     * {@code Optional.empty()} if no piece was removed
     */
    public Optional<Piece> push(Piece p) {
        Optional<Piece> kickedOffPiece = Optional.empty();
        if (stack.isEmpty() || p.getColor() == this.getColumnColor()) {
            addPiece(p);
        } else if (stack.size() == 1 && p.getColor() != this.getColumnColor()) {
            kickedOffPiece = pop();
            addPiece(p);
        }
        return kickedOffPiece;
    }

    private void addPiece(Piece p) {
        stack.push(p);
        p.setFieldPosition(fieldPosition);
        updateScreenPosition(p);
    }

    /**
     * Removes the top-most {@code Piece} from this column.
     *
     * @return an {@code Optional} containing the removed {@code Piece}, or
     * {@code Optional.empty()} if no piece was removed
     */
    public Optional<Piece> pop() {
        if (stack.isEmpty())
            return Optional.empty();
        return Optional.of(stack.pop());
    }

    private PieceColor getColumnColor() {
        return stack.isEmpty() ? null : stack.peek().getColor();
    }

    private void updateScreenPosition(Piece p) {
        Vector2D columnBase = Constants.columnPositionToPixel.get(p.getFieldPosition());
        int columnOffset = stack.size();
        if (p.getFieldPosition() < 12) {
            columnOffset -= 1;
        } else {
            columnOffset *= -1;
        }
        p.setPosition(columnBase.add(Vector2D.of(0, 40).scale(columnOffset)));
    }

}
