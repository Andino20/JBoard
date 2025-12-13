package plus.jboard.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Vector2DTests {

    @Test
    void arithmeticTest() {
        Vector2D a = Vector2D.of(3, 2);
        Vector2D b = new Vector2D(3.2, 7.9);
        Vector2D c = a.add(b);
        Vector2D d = a.sub(b);
        Vector2D e = a.scale(5);

        Assertions.assertEquals(3 + 3.2, c.x());
        Assertions.assertEquals(2 + 7.9, c.y());
        Assertions.assertEquals(3 - 3.2, d.x());
        Assertions.assertEquals(2 - 7.9, d.y());
        Assertions.assertEquals(3 * 5, e.x());
        Assertions.assertEquals(2 * 5, e.y());
    }

    @Test
    void inversionTest() {
        Vector2D a = Vector2D.of(3, 2);
        Vector2D b = a.inverse();
        Assertions.assertEquals(-3, b.x());
        Assertions.assertEquals(-2, b.y());
    }
}
