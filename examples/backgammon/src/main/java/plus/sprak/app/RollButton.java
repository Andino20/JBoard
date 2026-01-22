package plus.sprak.app;

import plus.jboard.core.GameObject;
import plus.jboard.math.Vector2D;
import plus.jboard.render.Sprite;

import java.io.IOException;
import java.nio.file.Path;

public class RollButton extends GameObject {

    private final Runnable onClick;

    public RollButton(Vector2D position, Runnable onClick) throws IOException {
        this.onClick = onClick;
        this.setPosition(position);
        this.sprite = new Sprite(Path.of("src", "main", "resources", "roll_button.png").toUri());
    }

    @Override
    public void onMouseClick(Vector2D position) {
        this.onClick.run();
    }

}
