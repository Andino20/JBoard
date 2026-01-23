package plus.jboard.core.anim.interpolators;

import plus.jboard.core.anim.Interpolator;
import plus.jboard.math.Vector2D;

public class LinearInterpolator {

    private LinearInterpolator() {
    }

    public static Interpolator<Integer> INT_LINEAR = (from, to, t) -> Math.toIntExact(Math.round(from + (to - from) * t));

    public static Interpolator<Double> DOUBLE_LINEAR = (from, to, t) -> from + (to - from) * t;

    public static Interpolator<Float> FLOAT_LINEAR = (from, to, t) -> (float) (from + (to - from) * t);

    public static Interpolator<Vector2D> VECTOR2D_LINEAR = (from, to, t) -> {
        double x = DOUBLE_LINEAR.interpolate(from.x(), to.x(), t);
        double y = DOUBLE_LINEAR.interpolate(from.y(), to.y(), t);
        return new Vector2D(x, y);
    };

}
