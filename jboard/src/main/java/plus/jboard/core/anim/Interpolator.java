package plus.jboard.core.anim;

@FunctionalInterface
public interface Interpolator<T> {
    T interpolate(T from, T to, double t);
}
