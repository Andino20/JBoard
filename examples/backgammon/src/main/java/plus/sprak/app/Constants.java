package plus.sprak.app;

import plus.jboard.math.Vector2D;

import java.util.List;
import java.util.Map;

public class Constants {
    private Constants() {}

    public static final Map<Integer, Vector2D> columnPositionToPixel = Map.ofEntries(
            Map.entry(-2, Vector2D.of(755, 5)),
            Map.entry(-1, Vector2D.of(343, 22)),
            Map.entry(0, Vector2D.of(641, 22)),
            Map.entry(1, Vector2D.of(591, 22)),
            Map.entry(2, Vector2D.of(540, 22)),
            Map.entry(3, Vector2D.of(490, 22)),
            Map.entry(4, Vector2D.of(439, 22)),
            Map.entry(5, Vector2D.of(389, 22)),
            Map.entry(6, Vector2D.of(282, 22)),
            Map.entry(7, Vector2D.of(231, 22)),
            Map.entry(8, Vector2D.of(181, 22)),
            Map.entry(9, Vector2D.of(130, 22)),
            Map.entry(10, Vector2D.of(79, 22)),
            Map.entry(11, Vector2D.of(29, 22)),
            Map.entry(12, Vector2D.of(29, 459)),
            Map.entry(13, Vector2D.of(79, 459)),
            Map.entry(14, Vector2D.of(130, 459)),
            Map.entry(15, Vector2D.of(181, 459)),
            Map.entry(16, Vector2D.of(231, 459)),
            Map.entry(17, Vector2D.of(282, 459)),
            Map.entry(18, Vector2D.of(389, 459)),
            Map.entry(19, Vector2D.of(439, 459)),
            Map.entry(20, Vector2D.of(490, 459)),
            Map.entry(21, Vector2D.of(540, 459)),
            Map.entry(22, Vector2D.of(591, 459)),
            Map.entry(23, Vector2D.of(641, 459)),
            Map.entry(24, Vector2D.of(343, 459)),
            Map.entry(25, Vector2D.of(715, 475))

    );
}
