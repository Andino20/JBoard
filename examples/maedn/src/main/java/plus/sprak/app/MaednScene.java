package plus.sprak.app;

import plus.jboard.core.Scene;
import plus.jboard.render.Drawable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class MaednScene extends Scene {

    public static class GameObject implements Drawable {
        private Image image;
        private int x;
        private int y;
        private int pos;
        private final Map<Integer, int[]> mapping;

        public GameObject(Image image, int x, int y) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.pos = -1;
            this.mapping = null;
        }

        public GameObject(Image image, int pos, Map<Integer, int[]> mapping) {
            this.image = image;
            this.x = -1;
            this.y = -1;
            this.pos = pos;
            this.mapping = mapping;
        }

        @Override
        public void draw(Graphics2D g2d) {
            if(this.x != -1){
                g2d.drawImage(image, x, y, null);
            }
            if(mapping != null){
                x = mapping.get(pos)[0];
                y = mapping.get(pos)[1];
                g2d.drawImage(image, x, y, null);
            }
        }

        public void move(int x, int y, boolean absolute){
            if(absolute){
                this.x = x;
                this.y = y;
            }
            else{
                this.x += x;
                this.y += y;
            }
        }

        public void move(int increment){
            if(this.mapping != null){
                pos = (pos + increment) % mapping.get(-1)[0]; //-1 of mapping contains the wraparoundvalue
            }
        }

        public void move(String specialvalue){
            if(this.pos != -1){
                pos = Integer.parseInt(specialvalue);
            }
        }

        public int[] getPos() {
            if(this.mapping != null){
                return mapping.get(pos);
            }
            else {
                return new int[]{x, y};
            }
        }
    }

    public MaednScene() throws IOException {
        BufferedImage piece = ImageIO.read(new File(Path.of("src", "main", "resources", "piece.png").toUri()));
        Image background = ImageIO.read(new File(Path.of("src", "main", "resources", "board.jpg").toUri()));
        Map<Integer, int[]> map = Map.ofEntries(
                Map.entry(-1, new int[]{15}),
                Map.entry(0,  new int[]{0, 128}),
                Map.entry(1,  new int[]{64, 128}),
                Map.entry(2,  new int[]{128, 128}),
                Map.entry(3,  new int[]{192, 128}),
                Map.entry(4,  new int[]{256, 128}),
                Map.entry(5,  new int[]{0, 192}),
                Map.entry(6,  new int[]{64, 192}),
                Map.entry(7,  new int[]{128, 192}),
                Map.entry(8,  new int[]{192, 192}),
                Map.entry(9,  new int[]{256, 192}),
                Map.entry(10, new int[]{0, 256}),
                Map.entry(11, new int[]{64, 256}),
                Map.entry(12, new int[]{128, 256}),
                Map.entry(13, new int[]{192, 256}),
                Map.entry(14, new int[]{256, 256}),

                Map.entry(-10, new int[]{512, 256})
        );


        background = background.getScaledInstance(720, 480, Image.SCALE_SMOOTH);

        ArrayList<GameObject> pieces = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            pieces.add(new GameObject(piece, i * 64, i * 64));
        }
        for (int i = 2; i < 5; i++) {
            pieces.add(new GameObject(piece, i, map));
        }

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                pieces.get(1).move(64, 0, false);
                pieces.get(4).move(1);
                check(pieces.get(4));
            }

            private void check(GameObject movedLast) {
                for (GameObject piece : pieces) {
                    if (piece != movedLast) {
                        if (Arrays.equals(piece.getPos(), movedLast.getPos())) {
                            piece.move("-10");
                        }
                    }
                }
            }
        }, 0, 2000);


        this.addRenderObject(new GameObject(background, 0, 0), 0);
        for (GameObject g : pieces) {
            this.addRenderObject(g, 1);
        }
    }

}
