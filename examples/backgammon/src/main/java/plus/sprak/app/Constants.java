package plus.sprak.app;

import plus.jboard.math.Vector2D;

import java.util.List;
import java.util.Map;

public class Constants {

    private Constants() {}

    public static final Map<PieceColor, List<Vector2D>> homeToPixel = Map.ofEntries(
            Map.entry(PieceColor.WHITE, List.of(
                    Vector2D.of(19, -10),
                    Vector2D.of(82, -10),
                    Vector2D.of(19, 32),
                    Vector2D.of(82, 32)
            )),
            Map.entry(PieceColor.BLACK, List.of(
                    Vector2D.of(596, -10),
                    Vector2D.of(661, -10),
                    Vector2D.of(596, 32),
                    Vector2D.of(661, 32)
            ))
    );

    public static final Map<Integer, Vector2D> fieldToPixel = Map.ofEntries(
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
}
