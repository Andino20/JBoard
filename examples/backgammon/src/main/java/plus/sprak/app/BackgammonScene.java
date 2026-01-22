package plus.sprak.app;

import plus.jboard.core.GameObject;
import plus.jboard.core.Scene;
import plus.jboard.math.Vector2D;
import plus.jboard.render.Sprite;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class BackgammonScene extends Scene {

    public static class Background extends GameObject {
        public Background() throws IOException {
            Image img = ImageIO.read(new File(Path.of("src", "main", "resources", "board.jpg").toUri()));
            this.sprite = new Sprite(img.getScaledInstance(720, 480, Image.SCALE_SMOOTH));
        }
    }

    public BackgammonScene() throws IOException {
        Background bg = new Background();
        DieSelector dieSelector = new DieSelector(Vector2D.of(335, 225));//TODO: Bitte lesen
                //Wir haben jetzt 2 Dice, darum: DieSelector handelt die dice
                //"Die" ist aufgeteilt in RollButton (würfelt) und DieSelector (wählt den Würfelwert zum Ziehen aus)
        RollButton rollButton = new RollButton(Vector2D.of(720, 215), dieSelector::roll);
        Board board = new Board(dieSelector);
        board.getAllPieces().forEach(p -> this.addGameObject(p, 1));
        dieSelector.getDice().forEach(p -> this.addGameObject(p, 2));
        this.addGameObject(rollButton, 2);
        this.addGameObject(bg, 0);
    }

}
