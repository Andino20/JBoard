package plus.jboard.math;

public record Rectangle(Vector2D origin, Vector2D dimensions) {
    public boolean isInside(Vector2D point) {
        return (point.x() >= origin.x() && point.x() <= origin.x() + dimensions.x()) &&
                (point.y() >= origin.y() && point.y() <= origin.y() + dimensions.y());
    }
}
