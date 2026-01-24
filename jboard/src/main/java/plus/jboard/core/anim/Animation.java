package plus.jboard.core.anim;

import plus.jboard.core.GameApplication;
import plus.jboard.core.Updatable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Animation<T> implements Updatable {

    private final double duration;
    private final Supplier<T> getter;
    private final Consumer<T> setter;
    private final Interpolator<T> interpolator;

    private final T target;
    private T from;

    private boolean active = false;
    private double time = 0.0f;

    public Animation(Supplier<T> getter, Consumer<T> setter, T target, double duration, Interpolator<T> interpolator) {
        this.getter = getter;
        this.setter = setter;
        this.duration = duration;
        this.target = target;
        this.interpolator = interpolator;
    }

    @Override
    public void update(double deltaTime) {
        if (active) {
            time += deltaTime;
            double t = Math.min(1.0, time / duration);
            setter.accept(interpolator.interpolate(from, target, t));
            if (time >= duration) {
                active = false;
                GameApplication.getInstance().removeUpdatable(this);
            }
        }
    }

    public void start() {
        this.active = true;
        from = getter.get();
        GameApplication.getInstance().addUpdatable(this);
    }

}
