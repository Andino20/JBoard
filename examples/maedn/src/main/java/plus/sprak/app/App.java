package plus.sprak.app;

import java.io.IOException;

public class App {
    private final Figure[] fields = new Figure[40];
    public int dice;

    public static void main(String[] args) throws IOException {
        //MaednScene gameScene = new MaednScene();
        //GameApplication app = new GameApplication("Mensch-ärgere-dich-nicht!", 720, 480, gameScene);
        //app.run();


    }

    public boolean can_move(int p, Color c) {
        if (c == fields[p].c) {
            System.out.println("Figure cant move, move figure from destined position first");
            return false;
        }
        fields[p].arr_pos = -1;
        move_home(fields[p]);
        return true;
    }

    private void move_home(Figure f) {
        //move in right home depending on its color
    }

    enum Color {
        Red, Green, Yellow, Blue
    }

    public class Figure {
        private final Color c;
        private int arr_pos;

        public Figure(Color c) {
            this.c = c;
            this.arr_pos = -1;
        }

        public void move(int dice) {
            if (dice == 6 && this.arr_pos < 0) {
                startmovement();
            } else {
                fields[arr_pos] = null;
                arr_pos += dice % 40;
                if (fields[arr_pos] == null || can_move(arr_pos, c)) {
                    fields[arr_pos] = this;
                }
            }
            drawarray();
        }

        private void drawarray() {
            for (Figure f : fields) {
                draw(f);//vielleicht so?
            }
        }

        public void roll_dice() {
            dice = (int) (Math.random() * 6) + 1;
            System.out.println(dice);
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
            } else {
                fields[startpos] = this;
            }
        }

    }
}