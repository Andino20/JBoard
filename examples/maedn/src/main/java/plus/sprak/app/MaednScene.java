package plus.sprak.app;

import plus.jboard.core.GameObject;
import plus.jboard.core.Scene;
import plus.jboard.math.Rectangle;
import plus.jboard.math.Vector2D;
import plus.jboard.render.Sprite;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class MaednScene extends Scene {

    private final Figure[] fields = new Figure[40];
    public int dice;//TODO WTF is wrong with you???
    public Map<Color, Figure[]> home_figures;
    public Map<Color, List<Vector2D>> homeToPixel = Map.ofEntries(
            Map.entry(Color.Blue, List.of(
                    Vector2D.of(16, 10),
                    Vector2D.of(79, 10),
                    Vector2D.of(16, 52),
                    Vector2D.of(79, 52)
            )),
            Map.entry(Color.Green, List.of(
                    Vector2D.of(593, 10),
                    Vector2D.of(658, 10),
                    Vector2D.of(593, 52),
                    Vector2D.of(658, 52)
            )),
            Map.entry(Color.Red, List.of(
                    Vector2D.of(593, 396),
                    Vector2D.of(658, 396),
                    Vector2D.of(593, 439),
                    Vector2D.of(658, 439)
            )),
            Map.entry(Color.Yellow, List.of(
                    Vector2D.of(16, 396),
                    Vector2D.of(79, 396),
                    Vector2D.of(16, 439),
                    Vector2D.of(79, 439)
            ))
    );

    public Map<Integer, Vector2D> fieldToPixelMap = Map.ofEntries(
            Map.entry(0, Vector2D.of(658, 247)),
            Map.entry(1, Vector2D.of(593, 247)),
            Map.entry(2, Vector2D.of(529, 247)),
            Map.entry(3, Vector2D.of(464, 247)),
            Map.entry(4, Vector2D.of(400, 247)),
            Map.entry(5, Vector2D.of(400, 290)),
            Map.entry(6, Vector2D.of(400, 333)),
            Map.entry(7, Vector2D.of(400, 376)),
            Map.entry(8, Vector2D.of(400, 419)),
            Map.entry(9, Vector2D.of(337, 419)),
            Map.entry(10, Vector2D.of(272, 419)),
            Map.entry(11, Vector2D.of(272, 376)),
            Map.entry(12, Vector2D.of(272, 333)),
            Map.entry(13, Vector2D.of(272, 290)),
            Map.entry(14, Vector2D.of(272, 247)),
            Map.entry(15, Vector2D.of(206, 247)),
            Map.entry(16, Vector2D.of(143, 247)),
            Map.entry(17, Vector2D.of(79, 247)),
            Map.entry(18, Vector2D.of(16, 247)),
            Map.entry(19, Vector2D.of(16, 203)),
            Map.entry(20, Vector2D.of(16, 160)),
            Map.entry(21, Vector2D.of(79, 160)),
            Map.entry(22, Vector2D.of(143, 160)),
            Map.entry(23, Vector2D.of(206, 160)),
            Map.entry(24, Vector2D.of(272, 160)),
            Map.entry(25, Vector2D.of(272, 117)),
            Map.entry(26, Vector2D.of(272, 75)),
            Map.entry(27, Vector2D.of(272, 32)),
            Map.entry(28, Vector2D.of(272, -10)),
            Map.entry(29, Vector2D.of(337, -10)),
            Map.entry(30, Vector2D.of(400, -10)),
            Map.entry(31, Vector2D.of(400, 32)),
            Map.entry(32, Vector2D.of(400, 75)),
            Map.entry(33, Vector2D.of(400, 117)),
            Map.entry(34, Vector2D.of(400, 160)),
            Map.entry(35, Vector2D.of(464, 160)),
            Map.entry(36, Vector2D.of(529, 160)),
            Map.entry(37, Vector2D.of(593, 160)),
            Map.entry(38, Vector2D.of(658, 160)),
            Map.entry(39, Vector2D.of(658, 203))
    );

    public static class Background extends GameObject {
        public Background() throws IOException {
            Image img = ImageIO.read(new File(Path.of("src", "main", "resources", "board.jpg").toUri()));
            this.sprite = new Sprite(img.getScaledInstance(720, 480, Image.SCALE_SMOOTH));
        }
    }

    public class Dice extends GameObject {
        public Dice() throws IOException {
            this.sprite = new Sprite(ImageIO.read(new File(Path.of("src", "main", "resources", "dice.jpg").toUri())));
            dice = 6;
            this.setPosition(Vector2D.of(150, 30));
        }

        @Override
        public void onMouseClick(Vector2D position) {
            roll_dice();
        }

        @Override
        public void draw(Graphics2D g2d) {
            g2d.setFont(new Font("Arial", Font.BOLD, 30));
            g2d.drawString("" + dice, (int) this.getPosition().x(), (int) this.getPosition().y());
        }

        @Override
        public plus.jboard.math.Rectangle getBoundingBox() {
            return new Rectangle(this.getPosition(), Vector2D.of(32, 32));
        }

        public void roll_dice() {
            dice = (int) (Math.random() * 6) + 1;
            System.out.println(dice);
        }
    }

    public boolean can_move(int p, Color c) {
        if (c == fields[p].c) {
            System.out.println("Figure cant move, move figure from destined position first");
            return false;
        }
        move_home(fields[p]);
        return true;
    }

    private void move_home(Figure f) {
        f.arr_pos = -1;
        Figure[] hf = home_figures.get(f.c);
        for (int i = 0; i < hf.length; i++) {
            if (hf[i] == null) {
                hf[i] = f;
                hf[i].setPosition(homeToPixel.get(f.c).get(i));
                break;
            }
        }

    }

    enum Color {
        Red, Green, Yellow, Blue
    }

    public class Figure extends GameObject {
        private final Color c;
        private int arr_pos;

        public Figure(Color c) throws IOException {
            this.c = c;
            this.arr_pos = -1;
            String filename = "";
            switch (c) {
                case Red -> filename = "piece_red.png";
                case Green -> filename = "piece_green.png";
                case Yellow -> filename = "piece_yellow.png";
                case Blue -> filename = "piece_blue.png";
            }
            this.sprite = new Sprite(Path.of("src", "main", "resources", filename).toUri());
        }

        @Override
        public void onMouseClick(Vector2D position) {
            move(dice);
        }

        public void move(int dice) {
            if (dice == 6 && this.arr_pos < 0) {
                startmovement();
            } else {
                int nextpos = (arr_pos + dice) % 40;
                if (fields[nextpos] == null || can_move(nextpos, c)) {
                    fields[arr_pos] = null;
                    changePos(nextpos);
                }
            }
        }

        public void startmovement() {
            int startpos = -1;
            switch (c) {
                case Red:
                    startpos = 0;
                    break;
                case Yellow:
                    startpos = 10;
                    break;
                case Blue:
                    startpos = 20;
                    break;
                case Green:
                    startpos = 30;
                    break;
            }
            if (fields[startpos] != null && fields[startpos].c == this.c) {
                System.out.println("Figure cant move, move figure from start position first");
            } else if (fields[startpos] != null) {
                move_home(fields[startpos]);
                changePos(startpos);
                removeFromHome();
            } else {
                changePos(startpos);
                removeFromHome();
            }
        }

        private void changePos(int startpos) {
            fields[startpos] = this;
            this.setPosition(fieldToPixelMap.get(startpos));
            this.arr_pos = startpos;
        }

        private void removeFromHome() {
            Figure[] hf = home_figures.get(c);
            for (int i = 0; i < hf.length; i++) {
                if (hf[i] == this) {
                    hf[i] = null;
                }
            }
        }

    }

    public MaednScene() throws IOException {
        Die d6 = new Die();
        Board board = new Board(d6);
        board.getAllFigures().forEach(f -> this.addGameObject(f, 1));
        addGameObject(d6, 2);

        Background bg = new Background();
        this.addGameObject(bg, 0);
    }

}
