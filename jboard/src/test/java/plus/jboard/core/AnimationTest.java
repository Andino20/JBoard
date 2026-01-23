package plus.jboard.core;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import plus.jboard.core.anim.Animation;
import plus.jboard.core.anim.interpolators.LinearInterpolator;

public class AnimationTest {

    @Getter
    @Setter
    private int x;

    @Test
    void linearAnimationTest() {
        this.setX(0);
        Animation<Integer> movement = new Animation<>(this::getX, this::setX, 10, 5.0f, LinearInterpolator.INT_LINEAR);
        movement.start();

        Assertions.assertEquals(0, x);
        movement.update(2.5f);
        Assertions.assertEquals(5, x);
        movement.update(2.5f);
        Assertions.assertEquals(10, x);
    }
}
