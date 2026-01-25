package plus.sprak.app;

import plus.jboard.core.Scene;
import plus.jboard.net.session.Session;

import java.io.IOException;

public class MaednScene extends Scene {

    public MaednScene(Session session, boolean host) throws IOException {
        Die d6 = new Die(host, session);
        Board board = new Board(d6, host, session);
        Background bg = new Background();

        board.getAllFigures().forEach(f -> this.addGameObject(f, 1));
        this.addGameObject(bg, 0);
        addGameObject(d6, 2);
    }

}
