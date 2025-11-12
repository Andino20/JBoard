package plus.jboard.graphics;

import plus.jboard.core.Piece;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RenderContext extends JPanel {

    private ArrayList<Piece> pieces = new ArrayList<>();

    public RenderContext() {
        this.setBackground(Color.WHITE);
    }

    public void addPiece(Piece p) {
        pieces.add(p);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render((Graphics2D) g);
    }

    private void render(Graphics2D g2d) {
        for (Piece piece : pieces) {
            g2d.drawImage(piece.getImage(), piece.getTransform().getX(), piece.getTransform().getY(), null);
        }
    }
}
