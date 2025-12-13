package plus.jboard.math;


public record Vector2D(double x, double y) {

    public static Vector2D of(Number x, Number y) {
        return new Vector2D(x.doubleValue(), y.doubleValue());
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(x + other.x, y + other.y);
    }

    public Vector2D sub(Vector2D other) {
        return new Vector2D(x - other.x, y - other.y);
    }

    public Vector2D scale(double scale) {
        return new Vector2D(x * scale, y * scale);
    }

    public Vector2D inverse() {
        return new Vector2D(-x, -y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
