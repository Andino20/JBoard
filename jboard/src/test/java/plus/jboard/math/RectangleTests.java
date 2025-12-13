package plus.jboard.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RectangleTests {

    @Test
    void testIsInside() {
        Rectangle a = new Rectangle(Vector2D.of(1, 1), Vector2D.of(5, 5));
        Vector2D p1 = new Vector2D(0.5, 0.5);
        Vector2D p2 = new Vector2D(1, 1);
        Vector2D p3 = new Vector2D(6, 6);
        Vector2D p4 = new Vector2D(6.01, 3);
        Vector2D p5 = new Vector2D(2, 6.01);
        Vector2D p6 = new Vector2D(3, 3);
        Vector2D p7 = new Vector2D(0.5, 3);
        Vector2D p8 = new Vector2D(3, 0.5);

        Assertions.assertFalse(a.isInside(p1));
        Assertions.assertTrue(a.isInside(p2));
        Assertions.assertTrue(a.isInside(p3));
        Assertions.assertFalse(a.isInside(p4));
        Assertions.assertFalse(a.isInside(p5));
        Assertions.assertTrue(a.isInside(p6));
        Assertions.assertFalse(a.isInside(p7));
        Assertions.assertFalse(a.isInside(p8));
    }
}
