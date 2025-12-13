package plus.sprak.app;

import plus.jboard.core.GameObject;
import plus.jboard.core.Scene;
import plus.jboard.math.Vector2D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MaednScene extends Scene {

    public MaednScene() throws IOException {
        ArrayList<Piece> pieces = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Piece p = new Piece(i);
            p.setPosition(new Vector2D(i * 64, i * 64));
            pieces.add(p);
        }

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Vector2D distance = Vector2D.of(64, 0);
                Piece p = pieces.get(1);
                p.setPosition(p.getPosition().add(distance));
            }
        }, 0, 2000);

        for (GameObject g : pieces) {
            this.addGameObject(g, 1);
        }
    }

}
