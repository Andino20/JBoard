package plus.sprak.app;

import plus.jboard.core.Scene;
import plus.jboard.net.session.Session;

import java.io.IOException;

public class MaednScene extends Scene {

    public MaednScene(Session session) throws IOException {
        Die d6 = new Die(true);
        Board board = new Board(d6, true, session);
        board.getAllFigures().forEach(f -> this.addGameObject(f, 1));
        addGameObject(d6, 2);

        Background bg = new Background();
        this.addGameObject(bg, 0);
    }

    public MaednScene(boolean host) throws IOException {
        Die d6 = new Die(host);
        Board board = new Board(d6, host, null);
        board.getAllFigures().forEach(f -> this.addGameObject(f, 1));
        addGameObject(d6, 2);

        Background bg = new Background();
        this.addGameObject(bg, 0);
    }

}
