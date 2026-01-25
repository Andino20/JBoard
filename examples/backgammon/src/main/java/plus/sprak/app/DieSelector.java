package plus.sprak.app;

import plus.jboard.core.GameApplication;
import plus.jboard.math.Vector2D;
import plus.jboard.net.NetworkEnvelope;
import plus.jboard.net.handler.MessageHandler;
import plus.jboard.net.session.Session;
import plus.sprak.app.messages.*;

import java.util.ArrayList;
import java.util.List;

public class DieSelector implements MessageHandler<GameMessage> {

    private static final int NUMBER_OF_DICE = 2;
    private static final Vector2D DIE_OFFSET = Vector2D.of(32, 0);

    private final boolean HOST;
    private final Session session;

    private final List<Die> dice = new ArrayList<>();

    private int selected = 0;
    private boolean isSelected = false;

    public DieSelector(Vector2D position, boolean host, Session session) {
        this.HOST = host;
        this.session = session;
        for (int i = 0; i < NUMBER_OF_DICE; i++) {
            int dieIdx = i;
            Die d6 = new Die(position.add(DIE_OFFSET.scale(i)), () -> this.selectDie(dieIdx));
            dice.add(d6);
        }
        GameApplication.getInstance().getMessageDispatcher().lateRegister(this);
    }

    public void roll() {
        if (!HOST) {
            RollRequest msg = new RollRequest();
            session.broadcast(msg);
            return;
        }
        dice.forEach(Die::roll);
        dice.forEach(die -> die.setActive(false));
        isSelected = false;

        RollMessage msg = new RollMessage();
        msg.setRoll0(dice.get(0).getRoll());
        msg.setRoll1(dice.get(1).getRoll());
        session.broadcast(msg);
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
        if (!HOST) {
            SelectionRequest msg = new SelectionRequest();
            msg.setSelection(idx);
            session.broadcast(msg);
            return;
        }
        dice.get(selected).setActive(false);
        this.selected = idx;
        dice.get(selected).setActive(true);
        isSelected = true;

        SelectionMessage msg = new SelectionMessage();
        msg.setSelection(idx);
        session.broadcast(msg);
    }

    @Override
    public Class<GameMessage> getAssociatedMessageType() {
        return GameMessage.class;
    }

    @Override
    public void handle(NetworkEnvelope<GameMessage> envelope) {
        if (envelope.message() instanceof RollRequest message) {
            if (HOST) {
                roll();
            }
        } else if (envelope.message() instanceof RollMessage message) {
            if (!HOST) {
                dice.get(0).setLastRoll(message.getRoll0());
                dice.get(1).setLastRoll(message.getRoll1());
                dice.forEach(die -> die.setActive(false));
                isSelected = false;
            }
        } else if (envelope.message() instanceof SelectionRequest message) {
            if (HOST) {
                selectDie(message.getSelection());
            }
        } else if (envelope.message() instanceof SelectionMessage message) {
            if (!HOST) {
                dice.get(selected).setActive(false);
                this.selected = message.getSelection();
                dice.get(selected).setActive(true);
                isSelected = true;
            }
        }
    }
}
