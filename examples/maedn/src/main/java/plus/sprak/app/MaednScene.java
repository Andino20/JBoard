package plus.sprak.app;

import plus.jboard.core.Scene;

import java.io.IOException;

public class MaednScene extends Scene {

    public MaednScene() throws IOException {
        Die d6 = new Die();
        Board board = new Board(d6);
        board.getAllFigures().forEach(f -> this.addGameObject(f, 1));
        addGameObject(d6, 2);

        Background bg = new Background();
        this.addGameObject(bg, 0);
    }

}
