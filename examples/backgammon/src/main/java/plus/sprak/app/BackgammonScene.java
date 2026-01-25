package plus.sprak.app;

import plus.jboard.core.Scene;
import plus.jboard.math.Vector2D;
import plus.jboard.net.session.Session;

import java.io.IOException;

public class BackgammonScene extends Scene {

    public BackgammonScene(Session session, boolean host) throws IOException {
        Background bg = new Background();
        DieSelector dieSelector = new DieSelector(Vector2D.of(335, 225), host, session);
        RollButton rollButton = new RollButton(Vector2D.of(720, 215), dieSelector::roll);
        Board board = new Board(dieSelector, host, session);

        this.addGameObject(bg, 0);
        board.getAllPieces().forEach(p -> this.addGameObject(p, 1));
        dieSelector.getDice().forEach(p -> this.addGameObject(p, 2));
        this.addGameObject(rollButton, 2);
    }

}
