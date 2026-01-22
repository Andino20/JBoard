package plus.sprak.app;

import plus.jboard.math.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class DieSelector {

    private static final int NUMBER_OF_DICE = 2;
    private static final Vector2D DIE_OFFSET = Vector2D.of(32, 0);

    private final List<Die> dice = new ArrayList<>();

    private int selected = 0;
    private boolean isSelected = false;

    public DieSelector(Vector2D position) {
        for (int i = 0; i < NUMBER_OF_DICE; i++) {
            int dieIdx = i;
            Die d6 = new Die(position.add(DIE_OFFSET.scale(i)), () -> this.selectDie(dieIdx));
            dice.add(d6);
        }
    }

    public void roll() {
        dice.forEach(Die::roll);
        dice.forEach(die -> die.setActive(false));
        isSelected = false;
    }

    public int getRoll() {
        if (isSelected) {
            return dice.get(selected).getRoll();
        }
        return 0;
    }

    public List<Die> getDice() {
        return dice;
    }

    private void selectDie(int idx) {
        dice.get(selected).setActive(false);
        this.selected = idx;
        dice.get(selected).setActive(true);
        isSelected = true;
    }

}
